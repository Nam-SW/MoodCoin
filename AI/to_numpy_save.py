import pandas as pd
import numpy as np
from AI_API import MoodCoin
import pickle

data = pd.read_excel('./dataset/dataset.xlsx')
# print(len(data['X_data']))
# Y = list(data['Y_data'])
# # nan = Y[-1]
# # print(Y.index(nan))
# print(pd.nan)
# Y = Y[:Y.index(np.nan)]
# print(len(Y))
data = data.dropna(how='any')

data2 = data[data['Y_data'] == 0].sample(frac=1).iloc[:100]
for i in range(1, 6):
    data2 = pd.concat([data2, data[data['Y_data'] == i].sample(frac=1).iloc[:100]])
data = data2

mc = MoodCoin()
X = [list(i) for i in mc.prepocessing(list(data['X_data']))]
Y = list(data['Y_data'])

print(len(X), len(Y))
for i in range(6):
    print(Y.count(i), end=' ')
# np.save('./dataset/X_data.npy', X, allow_pickle=True)
# np.save('./dataset/Y_data.npy', Y, allow_pickle=True)
with open('./dataset/x_data.pickle', 'wb') as f:
    pickle.dump(X, f)
with open('./dataset/y_data.pickle', 'wb') as f:
    pickle.dump(Y, f)

# x = np.load('./dataset/X_data.npy')
# y = np.load('./dataset/Y_data.npy')

with open('./dataset/x_data.pickle', 'rb') as f:
    x = pickle.load(f)
with open('./dataset/y_data.pickle', 'rb') as f:
    y = pickle.load(f)