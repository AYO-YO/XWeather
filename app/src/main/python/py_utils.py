import functools
import json
import requests


@functools.lru_cache()
def getData(url) -> json:
    session = requests.session()
    s = session.get(url)
    j = s.json()
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


def getAQI(city, need):
    url = f'http://web.juhe.cn/environment/air/pm?city={city}&key=d0785a3a4b1ad9ab70bd114cd4640b0d'
    j = getData(url)
    if need == 'pm25':
        return j['result'][0]['PM2.5']
    elif need == 'pm10':
        return j['result'][0]['PM10']
    elif need == 'state':
        return j['result'][0]['quality']
    elif need == 'time':
        return j['result'][0]['time'].split(' ')[1]
    else:
        return 'N/A'
