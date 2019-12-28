from AI_API import MoodCoin
from keras.models import load_model

mc = MoodCoin()

print(mc.prepocessing(['조심히 다녀와', '침착하게 이유를 말해봐...']))
print(mc.predict(['조심히 다녀와', '침착하게 이유를 말해봐...']))