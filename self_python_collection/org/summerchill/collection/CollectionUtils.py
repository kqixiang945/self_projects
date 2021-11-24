#!/usr/bin/python
# -*- coding: UTF-8 -*-蒋大帅 孔肖寒

import re
import subprocess
import datetime
from datetime import datetime
import re

import croniter
import datetime



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


def getClipboardData():
    p = subprocess.Popen(['pbpaste'], stdout=subprocess.PIPE)
    # retcode = p.wait()
    # retcode = p.communicate();
    data = p.stdout.read()
    return data


def get_clipboard_data():
    p = subprocess.Popen(['pbpaste'], stdout=subprocess.PIPE)
    data, _ = p.communicate()
    if p.returncode:  # pbpaste exited with non-zero status
        raise RuntimeError('pbpaste exited with: %d' % p.returncode)

    return data


def remove_blank_line():
    clipboard_str = get_clipboard_data()
    array = clipboard_str.decode().split('\n')
    str_conect = ''
    for rowOrigin in array:
        row = rowOrigin.strip()
        if len(rowOrigin) != 0:
            str_conect += row + '\n'
    return str_conect

def remove_duplicate_line():
    clipboard_str = get_clipboard_data()
    array = clipboard_str.decode().split('\n')
    uniqueLines = list(dict.fromkeys(array))
    # uniqueLines=set(array)
    str_conect = ''
    for line in uniqueLines:
        row = line.strip()
        if len(line) != 0:
            str_conect += row + '\n'
    return str_conect


def each_row_begin_and_end_add_char2(add_str):
    add_str_array = add_str.split(',')
    if (len(add_str_array) == 2):
        begin_str = add_str_array[0]
        end_str = add_str_array[1]

    str_conect = ''
    clipboard_str = getClipboardData()
    array = clipboard_str.decode().split('\n')
    for rowOrigin in array:
        row = rowOrigin.strip()
        if row.strip() != '':
            str_conect += begin_str + row + end_str + '\n'
    return str_conect


# sql结果集中 去掉空格 和 | 每列用tab键隔开
def rowhandle(symbol):
    symbol = symbol.strip(' ')
    str_conect = ''
    # clipboard_str = getClipboardData()
    clipboard_str = get_clipboard_data();
    array = clipboard_str.decode().split('\n')
    for rowOrigin in array:
        row = rowOrigin.strip()
        if row.strip() != '':
            if re.search('[\+][-\+]+', row):
                continue
            if (row.startswith('|')):
                row = row[1:]
            if (row.endswith('|')):
                row = row[:-1]
            if len(symbol) == 0:
                str_conect += row.replace('|', ',').replace(' ', '') + '\n'
            else:
                str_conect += row.replace('|', '\t').replace(' ', '') + '\n'
    return str_conect


def index_of(val, in_list):
    try:
        return in_list.index(val)
    except ValueError:
        return -1


def each_row_begin_and_end_substring(spec_str):
    spec_str_array = spec_str.split(',')
    if (len(spec_str_array) == 2):
        start_str = spec_str_array[0]
        end_str = spec_str_array[1]

    str_conect = ''
    clipboard_str = get_clipboard_data()
    array = clipboard_str.decode().split('\n')

    for rowOrigin in array:
        row = rowOrigin.strip()
        if row.strip() != '':
            start_index = index_of(start_str, row)
            end_index = index_of(end_str, row)
            if start_index == -1 | end_index == -1:
                str_conect += row + '\n'
            else:
                str_conect += row[start_index + len(start_str): end_index] + '\n'
    return str_conect



def validate_datetime_str(date_text):
    try:
        datetime.datetime.strptime(date_text, '%Y-%m-%d %H:%M:%S.%f')
        datetime.datetime.strptime(date_text, '%Y-%m-%d %H:%M:%S')
    except ValueError:
        raise ValueError("Incorrect data format, should be YYYY-MM-DD")

def validate_datetime_str2(date_text):
    try:
        datetime.fromisoformat(date_text)
    except ValueError:
        return -1





if __name__ == "__main__":

    str = remove_duplicate_line();
    print(str)

    now = datetime.datetime.now()
    # sched = '1 15 1,15 * *'    # at 3:01pm on the 1st and 15th of every month
    sched = '0 23 */2 * *'    # at 3:01pm on the 1st and 15th of every month
    cron = croniter.croniter(sched, now)
    for i in range(34):
        nextdate = cron.get_next(datetime.datetime)
        print (nextdate)
    # tt = run()
    # tt = one_row_to_multi_row(str2)
    # tt = each_row_begin_and_end_add_char('=======','++++++')
    # tt = each_row_begin_and_end_add_char2('======= , ++++++')
    # tt = rowhandle('\t')
    # tt = remove_blank_line()
    # tt = each_row_begin_and_end_substring('./app/,.conf')
    # print(tt)
    # sentence = 'Python programming is fun.'
    # # Substring is searched in 'gramming is fun.'
    # print(sentence.index('ing000'))
    # validate_datetime_str('2003-12-23 20:20:20.123')
    # validate_datetime_str('2003-12-23 20:20:20')
    # datetime.fromisoformat('2011-11-04')
    validate_datetime_str2('2003-12-23 20:20:20.123')

    text = 'gfgfdAAA1234ZZZuijAAA1299999ZZZjk'
    m = re.findall('AAA(.+?)ZZZ', text)
    if m:
        found = m.group(1)
        print(m.group(1))
        print(m.group(2))


