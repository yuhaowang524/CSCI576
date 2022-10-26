import math

import numpy as np

m = 8
n = 8
pi = math.pi

# input luminance block
lum_block = np.matrix([
    [188, 180, 155, 149, 179, 116, 86, 96],
    [168, 179, 168, 174, 180, 111, 86, 95],
    [150, 166, 175, 189, 165, 101, 88, 97],
    [163, 165, 179, 184, 135, 90, 91, 96],
    [170, 180, 178, 144, 102, 87, 91, 98],
    [175, 174, 141, 104, 85, 83, 88, 96],
    [153, 134, 105, 82, 83, 87, 92, 96],
    [117, 104, 86, 80, 86, 90, 92, 103]])

# Quantization table for luminance
k1_block = np.matrix([
    [16, 11, 10, 16, 124, 140, 151, 161],
    [12, 12, 14, 19, 126, 158, 160, 155],
    [14, 13, 16, 24, 140, 157, 169, 156],
    [14, 17, 22, 29, 151, 187, 180, 162],
    [18, 22, 37, 56, 168, 109, 103, 177],
    [24, 35, 55, 64, 181, 104, 113, 192],
    [49, 64, 78, 87, 103, 121, 120, 101],
    [72, 92, 95, 98, 112, 100, 103, 199]])


def dct_transform(block):
    ret = np.zeros((8, 8))
    for i in range(m):
        for j in range(n):
            sum_param = 0
            if i == 0:
                c_i = 1 / math.sqrt(m)
            else:
                c_i = math.sqrt(2 / m)
            if j == 0:
                c_j = 1 / math.sqrt(n)
            else:
                c_j = math.sqrt(2 / n)
            for x in range(m):
                for y in range(n):
                    dct_ij = block.item((x, y)) * math.cos((2 * x + 1) * i * pi / (2 * m)) * math.cos(
                        (2 * y + 1) * j * pi / (2 * n))
                    sum_param += dct_ij
            val = c_i * c_j * sum_param
            ret.itemset((i, j), val)
    return ret


def quantize_dct(dct_block, quan_block):
    ret = np.zeros((8, 8), dtype=int)
    for i in range(m):
        for j in range(n):
            val = round(dct_block.item((i, j)) / quan_block.item((i, j)))
            ret.itemset((i, j), val)
    return ret


def zig_zag_printer(block):
    ret = [[] for i in range(m + n - 1)]
    for i in range(m):
        for j in range(n):
            idx_sum = i + j
            if idx_sum % 2 == 0:
                ret[idx_sum].insert(0, block.item((i, j)))
            else:
                ret[idx_sum].append(block.item(i, j))
    for i in ret:
        for j in i:
            print(j, end=" ")


def main():
    dct_ret = dct_transform(lum_block)
    quan_ret = quantize_dct(dct_ret, k1_block)
    print("Question 1 quantization table output:")
    print(quan_ret)
    print("Question 2 zig-zag output:")
    zig_zag_printer(quan_ret)


if __name__ == "__main__":
    main()
