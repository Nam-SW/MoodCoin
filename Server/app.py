from flask import Flask, request
import firebase_admin
from firebase_admin import credentials, db
import json
from AI.AI_API import MoodCoin

app = Flask(__name__)
emotion = ['', '기쁨', '분노', '불안', '슬픔', '짜증']
moodcoin = MoodCoin() # AI 모델

DB_link = 'https://moodcoin-e9a22.firebaseio.com'
DB_cred = './moodcoin.json'
firebase_admin.initialize_app(credentials.Certificate(DB_cred), {
    'databaseURL' : DB_link
})

@app.route('/', methods=['POST'])
def chat_update():
    data = json.loads(str(request.data, encoding='utf8'))
    id = str(data['id'])
    talk = data['talk']
    print(id + ': ' + talk)

    pred = moodcoin.predict(talk)[0]
    print('예측 완료')
    if pred == 0: # 대화한 내용이 뜻이 없는 업무적 내용 등일 경우
        print('감정 없음')
        return ''

    print(emotion[pred])
    database = db.reference(id).get()
    print(database)
    keys = list(database.keys())
    print(keys)
    keys.remove('friends')
    print(keys)
    today = max(keys) # 가장 최근 날짜
    database[today][emotion[pred]] += 1
    print(database)
    db.reference(id).child(today).set(database[today])
    return ''
    


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, port=8080)