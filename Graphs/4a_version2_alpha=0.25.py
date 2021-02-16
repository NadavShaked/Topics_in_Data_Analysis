import numpy as np
import matplotlib.pyplot as plt

def main():
    fig, ax = plt.subplots()
    ax.set_title('Alpha 0.25 - Version 2')
    ax.set_xlabel('Number Of Pictures')
    ax.set_ylabel('Average Error Percentages')

    y_vals = [61.89, 63.93, 64.54, 66.54, 69.05, 76.49, 80.8, 86.55, 90.43]
    y_labels = ['60K', '40K', '20K', '10K', '5K', '1K', 500, 100, 20]
    y_position = [i for i in range(9)]

    ax.bar(y_position, y_vals, linewidth=0, capsize=5, align='center', color='green', alpha=0.6)
    ax.set_xticks(y_position)
    ax.set_xticklabels(y_labels)

    ax.invert_xaxis()
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()