import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os

def analysis(data_path, images_path):
    dataset = pd.read_csv(data_path +'/globalStatisticsOverTime.csv', header=None).values
    globalStatisticsOverTime = np.array(dataset[:, 0:4], dtype=np.float64)
    regionIds = [x[2:-1] for x in dataset[:,4]]
    regionIds = [x.split() for x in regionIds]
    regionIds = [list(map(lambda x: int(x), v)) for v in regionIds]
    time = globalStatisticsOverTime[:, 0]
    mean = globalStatisticsOverTime[:,1]
    variance = globalStatisticsOverTime[:, 2]
    count = globalStatisticsOverTime[:, 3]
    deviation = np.sqrt(variance)
    error = deviation

    variationTax = np.abs(deviation/mean)

    plt.plot(time, variationTax)
    plt.title("Global Variation Rate Over Time")
    plt.xlabel("Discrete Time")
    plt.ylabel("Variation Rate")
    plt.show()

    plt.errorbar(time, mean, error, ecolor='red', marker='o')
    plt.title("Mean Over Time")
    plt.xlabel("Discrete Time")
    plt.ylabel("Mean of Solutions Values")
    plt.show()

    plt.plot(time, count)
    plt.title("Solutions Over Time")
    plt.xlabel("Discrete Time")
    plt.ylabel("Number of Solutions")
    plt.show()

    regionsOverTime = np.genfromtxt(data_path + "/regionsOverTime.csv", delimiter=',')
    time = regionsOverTime[:,0]
    regions = regionsOverTime[:, 1]
    plt.plot(time, regions)
    plt.title("Number of Regions Over Time")
    plt.ylabel("Number of Regions")
    plt.xlabel("Discrete Time")
    plt.show()

    bestSolutionOverTime = pd.read_csv(data_path + "/globalBestSolutionOverTime.csv", delimiter=",", header=None, skipinitialspace=True).values
    bestSolutionOverTime = np.array(bestSolutionOverTime[:, 0:2], dtype=np.float64)
    time = bestSolutionOverTime[:, 0]
    function = bestSolutionOverTime[:, 1]
    for i in range(1, len(function)):
        function[i] = min(function[i], function[i - 1])

    plt.plot(time, function)
    plt.title("Best Solution Value Over Time")
    plt.ylabel("Function Value")
    plt.xlabel("Discrete Time")
    plt.show()

    maxTime = int(max(time))
    regions = int(max(max(regionIds)))

    regionMinValue = np.full([maxTime, regions + 1], np.nan)

    regionsBest = []

    for i in range(0, regions + 1):
        file = data_path + '/region-%d-bestSolutionOverTime.csv' % i
        if os.path.isfile(file):
            regionsBest.append(np.genfromtxt(file, delimiter=','))
        else:
            regionsBest.append(np.full([maxTime, 2], np.nan))

    for t in range(1, maxTime - 1):

        if t >= len(regionIds):
            break

        for r in regionIds[t]:
            # a region could live just for 1 time instant
            line = None
            if regionsBest[r].ndim > 1:
                i = np.argwhere(regionsBest[r][:,0] == t)
                if len(i) > 0:
                    i = i[0][0]
                    line = regionsBest[r][i,:]
                else:
                    line = None
            elif regionsBest[r].ndim == 1:
                line = regionsBest[r]

            if line is not None and len(line) > 1:
                regionMinValue[t, r] = np.nanmin([regionMinValue[t - 1, r], line[1]])
            else:
                regionMinValue[t, r] = regionMinValue[t - 1, r]


    plt.plot(regionMinValue)
    #plt.legend(['region-%d' % i for i in range(0, regions + 1)], loc=0)
    plt.title('Minimum Function Value of Each Region Over Time')
    plt.ylabel('Function Value')
    plt.xlabel('Discrete Time')
    plt.show()

    regionMean = np.full([maxTime, regions + 1], np.nan)
    regionVariance = np.full([maxTime, regions + 1], np.nan)

    regionStats = []
    for i in range(0, regions + 1):
        file = data_path + '/region-%d-regionStatisticsOverTime.csv' % i
        if os.path.isfile(file):
            regionStats.append(np.genfromtxt(file, delimiter=','))
        else:
            regionStats.append(np.full([maxTime, 4], np.nan))

    for t in range(1, maxTime - 1):

        if t >= len(regionIds):
            break

        for r in regionIds[t]:
            # a region could live just for 1 time instant
            line = None
            if regionStats[r].ndim > 1:
                i = np.argwhere(regionStats[r][:,0] == t)
                if len(i) > 0:
                    i = i[0][0]
                    line = regionStats[r][i, :]
                else:
                    line = None
            elif regionStats[r].ndim == 1:
                line = regionStats[r]

            if line is not None:
                regionMean[t, r] = line[1]
                regionVariance[t, r] = line[2]
            else:
                regionMean[t, r] = regionMean[t - 1, r]
                regionVariance[t, r] = regionVariance[t - 1, r]

    plt.plot(regionMean)
    #plt.legend(['region-%d' % i for i in range(0, regions + 1)], loc=0)
    plt.title('Average Function Value of Each Region Over Time')
    plt.ylabel('Average Function Value')
    plt.xlabel('Discrete Time')
    plt.show()

    regionStd = np.sqrt(regionVariance)
    regionVC = np.abs(regionStd/regionMean)

    mergeSplit = pd.read_csv(data_path + '/mergeSplitOverTime.csv', header=None).values

    plt.plot(regionVC)
    plt.plot(variationTax, 'b--')

    for line in mergeSplit:
        t = line[0]
        region = line[3]
        region = int(region.split('-')[1])

        if t >= len(regionVC):
            continue

        y = regionVC[t, region]

        if line[1] == ' MergeResult':
            if line[2] == ' sent':
                while np.isnan(y):
                    t -= 1
                    y = regionVC[t, region]
                plt.plot(t, y, 'rx')
            elif line[2] == ' received':
                plt.plot(t, y, 'bo')
        elif line[1] == ' RegionSplit':
            if line[2] == ' sent':
                plt.plot(t, y, 'gD')

    #plt.legend(['region-%d' % i for i in range(0, regions + 1)], loc=0)
    plt.title('Time Evolution of Each Region\'s Variation Rate')
    plt.ylabel('Variation Coefficient')
    plt.xlabel('Discrete Time')
    plt.show()

results_dir_template = "/data/results/%s%d/"

for exp in ["A"]:
    for inst in range(1,3):
        results_dir = results_dir_template % (exp, inst)
        runs = os.listdir(results_dir)
        runs = [x for x in runs if x.endswith("data")]

        for run in runs:
            run_dir = results_dir + run
            if len(os.listdir(run_dir)) > 1:
                try:
                    analysis(run_dir, "")
                except:
                    print("Error analysing execution %s" % run_dir)