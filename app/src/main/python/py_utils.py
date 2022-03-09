import functools
import json
import requests


@functools.lru_cache()
def getData(url) -> json:
    session = requests.session()
    header = {
        'User-Agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36 Edg/99.0.1150.36"
    }
    s = session.get(url, headers=header)
    j = json.loads(s.text)
    return j


def getHighTemp(j, day_num):
    return j['result']['future'][day_num]['temperature'].strip('℃').split('/')[1]


def getLowTemp(j, day_num):
    return j['result']['future'][day_num]['temperature'].strip('℃').split('/')[0]


def getWtState(j, day_num):
    data = j['result']['future'][day_num]['weather']
    if '转' in data:
        return data.split('转')[0]
    else:
        return data


def getWeather(local: str, need) -> str:
    url = f'http://apis.juhe.cn/simpleWeather/query?city={local}&key=7df95e3c4321b2217e7217ab08db04ca'
    j = getData(url)
    if j['error_code'] != '0':
        if need == 'state':
            return j['result']['realtime']['info']
        elif need == 'current_temp':
            return j['result']['realtime']['temperature']
        elif need == 'high_temp':
            return getHighTemp(j, 0)
        elif need == 'low_temp':
            return getLowTemp(j, 0)
        elif need == "day_1st_high":
            return getHighTemp(j, 1)
        elif need == "day_1st_low":
            return getLowTemp(j, 1)
        elif need == "day_2nd_high":
            return getHighTemp(j, 2)
        elif need == "day_2nd_low":
            return getLowTemp(j, 2)
        elif need == "day_3rd_high":
            return getHighTemp(j, 3)
        elif need == "day_3rd_low":
            return getLowTemp(j, 3)
        elif need == "day_1st_state":
            return getWtState(j, 1)
        elif need == "day_2nd_state":
            return getLowTemp(j, 2)
        elif need == "day_3rd_state":
            return getLowTemp(j, 3)
        elif need == "humidity":
            return j['result']['realtime']['humidity']
        elif need == "aqi":
            return j['result']['realtime']['aqi']
        elif need == "direct":
            return j['result']['realtime']['direct']
        elif need == "power":
            return j['result']['realtime']['power']
        else:
            return 'N/A'
    return 'N/A'


def getCityPinyin(city):
    with open('./city_info.json', 'rt', encoding='utf-8') as f:
        table = json.loads(f.read().strip())
    return table[city]['en']


def getCityCode(city):
    with open('./city_info.json', 'rt', encoding='utf-8') as f:
        table = json.loads(f.read().strip())
    return table[city]['code']


def getAQI(city, need):
    # url = f'http://web.juhe.cn/environment/air/pm?city={city}&key=d0785a3a4b1ad9ab70bd114cd4640b0d'
    url = f'https://api.help.bj.cn/apis/aqi2/?id={getCityCode(city)}'
    j = getData(url)
    if j["status"] != "412":
        if need == 'pm25':
            return j['data'][0]['val']
        elif need == 'pm10':
            return j['data'][5]['val']
        elif need == 'state':
            return j['lev']
        elif need == 'time':
            return j['update'][8:10] + ":" + j['update'][10:]
        else:
            return 'N/A'
    return 'N/A'


def getCity(x, y):
    url = f'https://restapi.amap.com/v3/geocode/regeo?key=f7e2de0fbef462404e89d6f465c20d76&location={x},{y}&poitype=&radius=0&extensions=all&batch=false&roadlevel=1'
    j = getData(url)
    data = j['regeocode']['addressComponent']['city']
    if data:
        return data[:-1]
    else:
        return j['regeocode']['addressComponent']['district'][:-1]
