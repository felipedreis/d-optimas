
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import os

for i in range(0, 100):
    file = "data/local/region-%d-solutionsOverTime.csv" % i
    if os.path.isfile(file):
        df = pd.read_csv(file, header=None)
        print(df)