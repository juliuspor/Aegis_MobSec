import socket
import time

def start_udp_server(ip, port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind((ip, port))
    print(f"Listening on UDP {ip}:{port}")

    while True:
        chunks = {}
        total_chunks = None
        file_type = None

        while True:
            data, addr = sock.recvfrom(8000)

            if not file_type:
                file_type = data[0]  # Set file type on first packet

            seq_num, expected_chunks = int.from_bytes(data[1:5], 'big'), int.from_bytes(data[5:9], 'big')
            chunk_data = data[9:]

            # Ensure consistent total_chunks across packets
            if total_chunks is None:
                total_chunks = expected_chunks
            elif total_chunks != expected_chunks:
                print("Inconsistent chunk count received. Ignoring this packet.")
                continue

            chunks[seq_num] = chunk_data
            print(f"Received chunk {seq_num + 1}/{total_chunks} from {addr}")

            # Check if all chunks are received
            if len(chunks) == total_chunks:
                # Reassemble the file in the correct order
                file_data = b''.join(chunks[i] for i in range(total_chunks))

                # Determine filename based on file type
                if file_type == 0x01:  # Image
                    filename = f"received_image_{int(time.time())}.jpg"
                elif file_type == 0x02:  # JSON
                    filename = f"received_{int(time.time())}.json"
                else:
                    print("Unknown file type received")
                    break

                # Write the file
                with open(filename, 'wb') as file:
                    file.write(file_data)
                print(f"File successfully reassembled and saved as {filename}.")
                break  # Reset for next file

if __name__ == "__main__":
    start_udp_server("127.0.0.1", 12345)
