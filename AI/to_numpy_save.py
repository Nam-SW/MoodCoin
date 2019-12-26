import pandas as pd
import numpy as np
from AI_API import MoodCoin

data = pd.read_excel('dataset/dataset.xlsx')
# print(len(data['X_data']))
# Y = list(data['Y_data'])
# # nan = Y[-1]
# # print(Y.index(nan))
# print(pd.nan)
# Y = Y[:Y.index(np.nan)]
# print(len(Y))
data = data.dropna(how='any')

mc = MoodCoin()
X = np.array(mc.prepocessing(list(data['X_data'])))
Y = data['Y_data'].to_numpy()

print(len(X), len(Y))
np.save('dataset/X_data.npy', X)
np.save('dataset/Y_data.npy', Y)