
import os
import codecs

def check_xml_files(start_dir):
    for root, dirs, files in os.walk(start_dir):
        if 'values' not in root:
            continue
        for file in files:
            if file.endswith('.xml'):
                path = os.path.join(root, file)
                try:
                    with open(path, 'rb') as f:
                        content = f.read()
                        if content.startswith(codecs.BOM_UTF8):
                            print(f"BOM_DETECTED: {path}")
                        
                        # Also check for other weird starting bytes
                        if not content.startswith(b'<?xml'):
                             # Check if it's just missing header or has garbage
                             pass
                except Exception as e:
                    print(f"ERROR: {path} - {e}")

check_xml_files('app/src/main/res')
