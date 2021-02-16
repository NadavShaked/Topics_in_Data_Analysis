import numpy as np
import matplotlib.pyplot as plt

def main():
    fig, ax = plt.subplots()
    ax.set_title('Version 1')
    ax.set_xlabel('Number Of Pictures')
    ax.set_ylabel('Average Error Percentages')

    y_vals = [21.66, 22.12, 22.25, 22.79, 23.12, 23.24, 25.56, 26.16, 32.3, 35.78, 42.42, 50.04, 62.79, 81.05, 85.03]
    y_labels = ['60K', '50K', '40K', '30K', '20K', '15K', '10K', '5K', '2K', '1K', 500, 250, 100, 50, 20]
    y_position = [i for i in range(15)]

    error_mark = [0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 1, 2.5, 9, 8, 2.5]

    ax.bar(y_position, y_vals, linewidth=0, yerr=error_mark, capsize=5, align='center', color='green', alpha=0.6, ecolor='black')
    ax.set_xticks(y_position)
    ax.set_xticklabels(y_labels)

    ax.invert_xaxis()
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()