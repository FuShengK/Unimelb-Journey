import pandas as pd ; import numpy as np ; import matplotlib.pyplot as plt
from sklearn.neighbors import KNeighborsClassifier as knc
from sklearn.tree import DecisionTreeClassifier as dtc
from sklearn.model_selection import train_test_split as tts
from sklearn.impute import SimpleImputer as si
from sklearn.metrics import accuracy_score as acs
from sklearn import preprocessing as ppc
# Delete column for country name(only one of the data) and year and time
world = pd.read_csv('world.csv', index_col=2, na_values=['..']).drop(['Time', 'Country Name'], axis=1)
life = pd.read_csv('life.csv', index_col=1).drop(['Year', 'Country'], axis=1)
for df in (world, life):
    # Strip the column(s) you're planning to join with
    df.index = df.index.str.strip()

# Join if they both have that Country Code and sort them by code    
combined = pd.merge(world, life, on='Country Code', how='inner')
combined.sort_values(by=['Country Code'], inplace=True)
data = combined.iloc[:, 1:-1].astype(float) ; classlabel = combined.iloc[:, -1]
# impute data
imputed = si(missing_values=np.nan, strategy='median').fit(data).transform(data)
col_data = np.column_stack(imputed)

# export CSV
ans = [[], [], [], []]
for col, index in enumerate(data.columns):
    ans[0].append(index) ; ans[1].append(round(np.median(col_data[col]), 3))
    ans[2].append(round(np.mean(col_data[col]), 3)) ; ans[3].append(round(np.var(col_data[col]), 3))
pd.DataFrame({"feature":ans[0], "median":ans[1], 
              'mean':ans[2], 'variance':ans[3]}).to_csv(r'task2a.csv', index = False)
    
X_train, X_test, y_train, y_test = tts(imputed, classlabel, train_size=(0.7), random_state=200)
scaler = ppc.StandardScaler().fit(X_train)
X_train = scaler.transform(X_train) ; X_test = scaler.transform(X_test)

y_pred_dt = dtc(random_state=200, max_depth=3).fit(X_train, y_train).predict(X_test)
y_pred_3 = knc(n_neighbors=3).fit(X_train, y_train).predict(X_test)
y_pred_7 = knc(n_neighbors=7).fit(X_train, y_train).predict(X_test)

print("Accuracy of decision tree: {:.3f}".format(acs(y_test, y_pred_dt)))
print("Accuracy of k-nn (k=3): {:.3f}".format(acs(y_test, y_pred_3)))
print("Accuracy of k-nn (k=7): {:.3f}".format(acs(y_test, y_pred_7)))