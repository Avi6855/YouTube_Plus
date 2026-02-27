package com.avinashpatil.app.youtube.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.FragmentCommentsBinding
import com.avinashpatil.app.youtube.extensions.formatShort
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.adapters.CommentsPagingAdapter
import com.avinashpatil.app.youtube.ui.models.CommentsViewModel
import com.avinashpatil.app.youtube.ui.sheets.CommentsSheet

class CommentsMainFragment : Fragment(R.layout.fragment_comments) {
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommentsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCommentsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.commentsRV.layoutManager = layoutManager
        binding.commentsRV.setItemViewCacheSize(20)

        val commentsSheet = parentFragment as? CommentsSheet
        commentsSheet?.binding?.btnScrollToTop?.setOnClickListener {
            // scroll back to the top / first comment
            _binding?.commentsRV?.smoothScrollToPosition(POSITION_START)
            viewModel.setCommentsPosition(POSITION_START)
        }

        binding.commentsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                // hide or show the scroll to top button
                commentsSheet?.binding?.btnScrollToTop?.isVisible = firstVisiblePosition != 0
                viewModel.setCommentsPosition(firstVisiblePosition)

                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        commentsSheet?.updateFragmentInfo(false, getString(R.string.comments))

        val commentPagingAdapter = CommentsPagingAdapter(
            isReplies = false,
            channelAvatar = requireArguments().getString(IntentData.channelAvatar),
            handleLink = {
                setFragmentResult(
                    CommentsSheet.HANDLE_LINK_REQUEST_KEY,
                    bundleOf(IntentData.url to it),
                )
            },
            saveToClipboard = { comment ->
                viewModel.saveToClipboard(view.context, comment)
            },
            navigateToChannel = { comment ->
                NavigationHelper.navigateChannel(view.context, comment.commentorUrl)
                setFragmentResult(CommentsSheet.DISMISS_SHEET_REQUEST_KEY, Bundle.EMPTY)
            },
            navigateToReplies = { comment, channelAvatar ->
                if (comment.repliesPage != null) {
                    val args = bundleOf(
                        IntentData.videoId to viewModel.videoIdLiveData.value,
                        IntentData.comment to comment,
                        IntentData.channelAvatar to channelAvatar
                    )
                    parentFragmentManager.commit {
                        viewModel.setLastOpenedCommentRepliesId(comment.commentId)
                        replace(
                            R.id.commentFragContainer,
                            CommentsRepliesFragment().apply { arguments = args }
                        )
                        addToBackStack(null)
                    }
                    commentsSheet?.updateFragmentInfo(
                        true,
                        getString(R.string.replies)
                    )
                }
            },
        )
        binding.commentsRV.adapter = commentPagingAdapter

        commentPagingAdapter.addLoadStateListener { loadStates ->
            binding.progress.isVisible = loadStates.refresh is LoadState.Loading

            val refreshState = loadStates.source.refresh
            if (refreshState is LoadState.NotLoading && commentPagingAdapter.itemCount > 0) {
                viewModel.currentCommentsPosition.value?.let { position ->
                    binding.commentsRV.scrollToPosition(maxOf(position, POSITION_START))
                }
            }

            if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached && commentPagingAdapter.itemCount == 0) {
                binding.errorTV.text = getString(R.string.no_comments_available)
                binding.errorTV.isVisible = true
            }
        }

        viewModel.currentCommentsPosition.observe(viewLifecycleOwner) {
            commentsSheet?.binding?.btnScrollToTop?.isVisible = it != 0
        }

        viewModel.commentsLiveData.observe(viewLifecycleOwner) {
            commentPagingAdapter.submitData(lifecycle, it)
        }

        viewModel.commentCountLiveData.observe(viewLifecycleOwner) { commentCount ->
            if (commentCount == null) return@observe

            commentsSheet?.updateFragmentInfo(
                false,
                getString(R.string.comments_count, commentCount.formatShort())
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val POSITION_START = 0
    }
}
