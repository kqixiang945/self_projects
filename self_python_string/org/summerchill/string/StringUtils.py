str = '''dx_e0_dim_d02_zhuge_appid_event_product_mapping
    hv_e1_dwd_dim_dim_d02_zhuge_appid_event_product_mapping
    device_synchro_statistics'''


def multi_row_to_one_row():
    str_conect = ''
    array = str.split('\n')
    for rowOrigin in array:
        row = rowOrigin.strip()
        str_conect += row + ','
    return str_conect

str2 = "'www.google.com.hk','www.google.com.hk999,'www.google.com.hk','www.google.com.hk','www.google.com.hk','www.google.com.hk'"


def one_row_to_multi_row(str2):
    str_conect = ''
    array = str2.split(',')
    for rowOrigin in array:
        row = rowOrigin.strip()
        if row.startswith('\'') and row.endswith('\'') or row.startswith("\"") and row.endswith("\""):
            row = row[1:-1]
        str_conect += row + '\n' 
    return str_conect


def each_row_begin_and_end_add_char(begin_str, end_str):
    str_conect = ''
    array = str.split('\n')
    for rowOrigin in array:
        row = rowOrigin.strip()
        if row.strip() != '':
            str_conect += begin_str + row + end_str + '\n'
    return str_conect


