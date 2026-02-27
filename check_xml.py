
import os

def check_xml_files(start_dir):
    for root, dirs, files in os.walk(start_dir):
        if 'values' not in root:
            continue
        for file in files:
            if file.endswith('.xml'):
                path = os.path.join(root, file)
                try:
                    with open(path, 'r', encoding='utf-8') as f:
                        content = f.read()
                        if not content.strip().startswith('<?xml'):
                            print(f"MISSING_HEADER: {path}")
                        elif content.strip().startswith('<?xml') and not content.startswith('<?xml'):
                             print(f"LEADING_WHITESPACE: {path}")
                except Exception as e:
                    print(f"ERROR: {path} - {e}")

check_xml_files('app/src/main/res')
