import os

import matplotlib.pyplot as plt
import pandas as pd
from pandas.errors import EmptyDataError


def get_boxplot_data(base_dir, experiment):
    results = []
    samples = [1, 2, 3]
    matrix =[]
    for i in samples:
        exp_dir = "%s/%s%d/" % (base_dir, experiment, i)

        instances = os.listdir(exp_dir)
        instances = [x for x in instances if x.endswith("data")]

        values = []
        agents = []
        for instance in instances:
            file = exp_dir + instance + "/globalBestSolutionOverTime.csv"
            print("Getting data from " + file)
            if os.path.isfile(file):
                try:
                    dataset = pd.read_csv(file, header=None)
                    values.append(dataset.iloc[-1, 1])
                    agents.append(dataset.iloc[-1, 2])
                except EmptyDataError:
                    print(file + " is empty")

        matrix.append(agents[0:20])
        matrix.append(values[0:20])

        df = pd.DataFrame(data={"agents": agents, "values": values})
        results.append(df)

    sampleNames = ["%s%d" % (experiment, i) for i in samples]
    cols = pd.MultiIndex.from_product([sampleNames, ["agents", "values"]])
    #res = pd.DataFrame(data=np.transpose(matrix), columns=cols)
    #print (res)
    return results

def boxplot(data, title, xlabel, ylabel, file):
    plt.boxplot([data[0]["values"], data[1]["values"], data[2]["values"]], labels=[2000, 4000, 6000])
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.savefig(file)
    plt.show()

def agent_stats(data):
    pass

exp_a = get_boxplot_data("/data/results", "A")
boxplot(exp_a, "Boxplot of 30 simulations with stoped at \n2000, 4000 and 6000 time units solving EggHolder function",
        "Discrete Time", "Function Value", "experiments/preliminar/graphics/A.png")

exp_b = get_boxplot_data("/data/results", "B")
boxplot(exp_b, "Boxplot of 30 simulations with stoped at \n2000, 4000 and 6000 time units solving XinSheYang function",
        "Discrete Time", "Function Value", "experiments/preliminar/graphics/B.png")

exp_c = get_boxplot_data("/data/results", "C")
boxplot(exp_c, "Boxplot of 30 simulations with stoped at \n2000, 4000 and 6000 time units solving Number Partition Problem",
        "Discrete Time", "Function Value", "experiments/preliminar/graphics/C.png")