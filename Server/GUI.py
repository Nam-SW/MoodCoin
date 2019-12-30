import sys
from PyQt5.QtWidgets import QApplication, QWidget, QGridLayout, QLabel, QPushButton, QMessageBox, QTextBrowser
from app import app

class MyApp(QWidget):
    def __init__(self):
        super().__init__()

        self.initUI()

    def initUI(self):
        grid = QGridLayout()
        self.setLayout(grid)

        self.open_server = QPushButton('서버 열기', self)
        self.close_server = QPushButton('서버 닫기', self)
        self.clear_log = QPushButton('로그 지우기', self)

        self.server_status = QLabel('닫힘')
        self.logbox = QTextBrowser(self)

        self.open_server.clicked.connect(self.log_append)
        self.close_server.clicked.connect(self.logbox.clear)
        self.clear_log.clicked.connect(self.logbox.clear)

        grid.addWidget(self.logbox, 0, 0, 4, 5)
        grid.addWidget(self.open_server, 0, 5)
        grid.addWidget(self.close_server, 1, 5)
        grid.addWidget(self.clear_log, 2, 5)
        grid.addWidget(self.server_status, 3, 5)


        self.setWindowTitle('test')
        # self.setGeometry(300, 300, 300, 250)
        self.show()
    
    def log_append(self):
        app.run(host='0.0.0.0', debug=True, port=8080)
        self.logbox.append('test')
    
    def server_closing(self):
        
        self.logbox.clear()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = MyApp()
    sys.exit(app.exec_())