import pandas as pd ; import numpy as np ; import matplotlib.pyplot as plt
from sklearn.feature_selection import mutual_info_classif as mic
from sklearn.feature_selection import SelectKBest as skb
from sklearn.preprocessing import PolynomialFeatures as pf
from sklearn.preprocessing import StandardScaler as ss
from sklearn.preprocessing import LabelEncoder as le
from sklearn.model_selection import train_test_split as tts
from sklearn.neighbors import KNeighborsClassifier as knc
from sklearn.tree import DecisionTreeClassifier as dtc
from sklearn.metrics import accuracy_score as acs
from sklearn.impute import SimpleImputer as si
from sklearn.cluster import KMeans as km
from sklearn.decomposition import PCA
# Delete column for country name and year and time
world = pd.read_csv('world.csv', index_col=2, na_values=['..']).drop(['Time', 'Country Name'], axis=1)
life = pd.read_csv('life.csv', index_col=1).drop(['Year', 'Country'], axis=1)
for df in (world, life):
    # Strip the column(s) you're planning to join with
    df.index = df.index.str.strip()
    
combined = pd.merge(world, life, on='Country Code', how='inner')
combined.sort_values(by=['Country Code'], inplace=True)
data = combined.iloc[:, 1:-1].astype(float) ; classlabel = combined.iloc[:, -1]
imputed = si(missing_values=np.nan, strategy='mean').fit(data).transform(data)
target = le().fit(classlabel).transform(classlabel)

# createa feature by k-mean clustering use k=3
f_clusterlabel = km(n_clusters=3).fit_predict(imputed)
f_clusterlabel = f_clusterlabel.reshape(len(f_clusterlabel),1)
# create feature interaction 
feature_interaction = pf(include_bias=False, interaction_only=True).fit_transform(imputed)

# select 4 features using ANOVA F-value (add features to one array) and split and apply 3-NN for features
features = np.append(feature_interaction, f_clusterlabel, axis=1)
fs = skb(mic, k=4) ; feature_selection = fs.fit_transform(features, target)
fs_X_train, fs_X_test, fs_y_train, fs_y_test = tts(feature_selection, target, train_size=(0.7), random_state=130)
fs_scaler = ss().fit(fs_X_train) ; fs_X_train = fs_scaler.transform(fs_X_train) ; fs_X_test=fs_scaler.transform(fs_X_test)
pred_feature_selection = knc(n_neighbors=3).fit(fs_X_train, fs_y_train).predict(fs_X_test)

# select 4 features using PCA and split and apply 3-NN for features
pca_feature = PCA(n_components=4).fit_transform(imputed)
pca_X_train, pca_X_test, pca_y_train, pca_y_test = tts(pca_feature, target, train_size=(0.7), random_state=130)
pred_pca = knc(n_neighbors=3).fit(pca_X_train, pca_y_train).predict(pca_X_test)

# select first 4 feature from original features and split and apply 3-NN for features
four_orginal = imputed[:, 0:4]
X_train, X_test, y_train, y_test = tts(four_orginal, target, train_size=(0.7), random_state=130)
scaler = ss().fit(X_train) ; X_train = scaler.transform(X_train) ; X_test=scaler.transform(X_test)
pred_four_original = knc(n_neighbors=3).fit(X_train, y_train).predict(X_test)

print("Accuracy of feature engineering: {:.3f}".format(acs(fs_y_test, pred_feature_selection)))
print("Accuracy of PCA: {:.3f}".format(acs(pca_y_test, pred_pca)))
print("Accuracy of first four features: {:.3f}".format(acs(y_test, pred_four_original)))

# Do K-mean quality measure
distortions = []
for i  in range(1,11):                # compute cluster up to 10
    check = km(n_clusters=i).fit(imputed)
    distortions.append(check.inertia_)   # get SSE using .interia_    
    
plt.plot(range(1,11),distortions,marker='o')
plt.xlabel('Number of clusters') ; plt.ylabel('Distortion')
#find elbow from the graph here K=3
plt.savefig('task2bgraph1.png',bbox_inches='tight')

plt.clf()
# plot the scores
plot_feature = [i for i in range(len(fs.scores_))]
plt.bar(plot_feature, fs.scores_)
plt.xlabel('Features') ; plt.ylabel('Mutual Information Feature Importance')
plt.savefig('task2bgraph2.png',bbox_inches='tight')