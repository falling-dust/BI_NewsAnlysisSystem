import socket
import json
import time


def print_csv_data(file_path):
    with open(file_path, 'r') as f:
        first_line = True
        for line in f:
            if first_line:
                first_line = False
                continue
            line = line.strip()
            if line:
                data = line.split(',')
                user_id = data[0]
                news_id = data[1]
                dwell_time = data[2]
                end_time = data[3]
                log_message = f"user_id: {user_id}, news_id: {news_id}, dwell_time: {dwell_time}, end_time: {end_time}\n"
                print(log_message)
                #sock.sendall(log_message.encode())
                time.sleep(0.1)


if __name__ == '__main__':
    # Create a socket object
    #sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Connect to the Flume agent
    #sock.connect(('localhost', 4444))

    # Print the CSV data
    print_csv_data('log.csv')

    # Close the socket
    #sock.close()