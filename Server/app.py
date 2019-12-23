from flask import Flask, request, jsonify
import firebase_admin
from firebase_admin import credentials, db

app = Flask(__name__)
emotion = ['', '기쁨', '분노', '불안', '슬픔', '짜증']
# moodcoin = MoodCoin() # AI 모델

DB_link = 'https://moodcoin-e9a22.firebaseio.com'
DB_cred = './moodcoin.json'
firebase_admin.initialize_app(credentials.Certificate(DB_cred), {
    'databaseURL' : DB_link
})

@app.route('/', methods=['POST'])
def chat_update():
    id = request.form['id']
    talk = request.form['talk']
    print(id, talk)

    # pred = moodcoin.predict(talk)
    # if pred == 0: # 대화한 내용이 뜻이 없는 업무적 내용 등일 경우
    #     return

    # data = db.reference(id).get()
    # today = max(data.keys()) # 가장 최근 날짜
    # data[today][emotion[1]] += 1
    # db.reference(id).child(today).set(data[today])
    


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, port=8080)