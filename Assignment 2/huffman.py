import heapq


class node:
    def __init__(self, freq, symbol, left=None, right=None):
        self.freq = freq
        self.symbol = symbol
        self.left = left
        self.right = right
        self.huff = ''

    def __lt__(self, nxt):
        return self.freq < nxt.freq


def print_nodes(node, string=''):
    new_str = string + str(node.huff)
    if node.left:
        print_nodes(node.left, new_str)
    if node.right:
        print_nodes(node.right, new_str)
    if not node.left and not node.right:
        print(f"{node.symbol}-{new_str}-{node.freq}-{len(new_str)}")


def freq_counter(input_string):
    string_list = input_string.split(" ")
    count_list = dict()
    for word in string_list:
        if word not in count_list.keys():
            count_list[word] = 1
        else:
            count_list[word] += 1
    return [word for word in count_list.keys()], [num for num in count_list.values()]


def main():
    input_str_one = "Hello from Paris I got this postcard from the Louvre You would love Paris I hope to hear from you"
    # input_str_two = "IN PARIS POSTCARD FROM LOUVRE STOP YOU WOULD LOVE STOP HOPE HEAR FROM YOU STOP"
    words_list, freq_list = freq_counter(input_str_one)

    # chars = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']
    # freq = [28, 20, 17, 17, 10, 5, 2, 1]

    nodes = []
    for i in range(len(words_list)):
        heapq.heappush(nodes, node(freq_list[i], words_list[i]))
    while len(nodes) > 1:
        left = heapq.heappop(nodes)
        right = heapq.heappop(nodes)
        left.huff = 0
        right.huff = 1
        new_node = node(left.freq + right.freq, left.symbol + right.symbol, left, right)
        heapq.heappush(nodes, new_node)
    print_nodes(nodes[0])


if __name__ == "__main__":
    main()
