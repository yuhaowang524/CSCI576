import matplotlib.pyplot as plt
import math

import numpy as np


def f(x):
    return -x ** 2 * math.log(x ** 2, 2) + x ** 2 * math.log((1 - x ** 2), 2) - math.log((1 - x ** 2), 2)


def k2d(x):
    return 2 * x * (math.log((1 - x ** 2), 2) - math.log(x ** 2, 2))


def k3d(x):
    return 3 * x ** 2 * (math.log((1 - x ** 3), 2) - math.log(x ** 3, 2))


def k4d(x):
    return 4 * x ** 3 * (math.log((1 - x ** 4), 2) - math.log(x ** 4, 2))


def main():
    x2 = np.linspace(-0.99999, 0.99999)
    x3 = np.linspace(0.00001, 0.99999)
    x4 = np.linspace(-0.99999, 0.99999)
    f2 = np.vectorize(k2d)
    f3 = np.vectorize(k3d)
    f4 = np.vectorize(k4d)
    plt.plot(x2, f2(x2), color='blue', label='k = 2')
    plt.plot(x3, f3(x3), color='red', label='k = 3')
    plt.plot(x4, f4(x4), color='black', label='k = 4')
    plt.legend(loc='upper right')
    plt.xlim([-1.5, 1.5])
    plt.ylim([-5, 5])
    plt.show()


if __name__ == "__main__":
    main()
