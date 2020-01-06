import discord
from firebase_admin import credentials, db, initialize_app
from AI.AI_API import MoodCoin

client = discord.Client()
# 생성된 토큰을 입력해준다.
token = "NjYxODAyNzgyNjA0Mzk0NTE2.XhLauw.yleBUN1ok6onnbuMSH5OfuMUSdw"

# AI 필요 변수
moodcoin = MoodCoin()
emotion = ['', '기쁨', '분노', '불안', '슬픔', '짜증']

# 파이어베이스 연결 변수
DB_link = 'https://moodcoin-e9a22.firebaseio.com'
DB_cred = './moodcoin.json'
initialize_app(credentials.Certificate(DB_cred), {
    'databaseURL' : DB_link
})

# 봇이 구동되었을 때 보여지는 코드
@client.event
async def on_ready():
    print("다음으로 로그인합니다")
    print(client.user.name)
    print(client.user.id)
    print("================")

# 봇이 특정 메세지를 받고 인식하는 코드
@client.event
async def on_message(message):
    userid = message.author.id
    # 메세지를 보낸 사람이 봇일 경우 무시한다
    if message.author.bot:
        return None
    
    if message.content:
        data = {'id' : userid,
                'talk': message.content
                }

        print(str(data['id']) + ': ' + data['talk'])

        pred = moodcoin.predict(data['talk'])[0]
        if pred == 0: # 대화한 내용이 뜻이 없는 업무적 내용 등일 경우
            print('감정 없음\n')
            return

        database = db.reference(str(data['id'])).get()
        keys = list(database.keys())
        keys.remove('friends')
        today = max(keys) # 가장 최근 날짜
        database[today][emotion[pred]] += 1
        db.reference(str(data['id'])).child(today).set(database[today])
        print(emotion[pred] + '\n')



if __name__ == "__main__":
    client.run(token)