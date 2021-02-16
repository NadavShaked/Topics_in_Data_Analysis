import numpy as np
import matplotlib.pyplot as plt

def main():
    fig, ax = plt.subplots()
    ax.set_title('Version 2')
    ax.set_xlabel('Number Of Pictures')
    ax.set_ylabel('Average Error Percentages')

    y_vals = [12.23, 12.31, 12.35, 12.19, 13, 12.32, 13.55, 15.27, 18.47, 22.57, 31.47333, 38.68, 56.634, 71.246, 81.516]
    y_labels = ['60K', '50K', '40K', '30K', '20K', '15K', '10K', '5K', '2K', '1K', 500, 250, 100, 50, 20]
    y_position = [i for i in range(15)]

    error_mark = [0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 7, 15, 11, 6]

    ax.bar(y_position, y_vals, linewidth=0, yerr=error_mark, capsize=5, align='center', color='green', alpha=0.6, ecolor='black')
    ax.set_xticks(y_position)
    ax.set_xticklabels(y_labels)

    ax.invert_xaxis()
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()