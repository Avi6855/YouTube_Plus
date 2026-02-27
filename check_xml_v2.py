
import os

def check_xml_files(start_dir):
    count = 0
    for root, dirs, files in os.walk(start_dir):
        if 'values' not in root:
            continue
        for file in files:
            path = os.path.join(root, file)
            if not file.endswith('.xml'):
                print(f"NON_XML_FILE: {path}")
                continue
            
            try:
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    if not content.strip():
                        print(f"EMPTY_FILE: {path}")
                    elif not content.strip().startswith('<?xml'):
                        print(f"MISSING_HEADER: {path}")
                        # Print first few chars to debug
                        print(f"  Content start: {repr(content[:20])}")
                    elif content.strip().startswith('<?xml') and not content.startswith('<?xml'):
                         print(f"LEADING_WHITESPACE: {path}")
                    else:
                        count += 1
            except Exception as e:
                print(f"ERROR: {path} - {e}")
    print(f"Checked {count} valid XML files.")

check_xml_files('app/src/main/res')
