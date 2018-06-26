import subprocess
import os.path

def main():
    filepath = input("file path: ")
    if(filepath == ""):
        compileAntlr()
        compileJava()
        exit()    
    if(os.path.isfile(filepath)):
        compileAntlr()
        compileJava()
        exeJava(filepath)
        cleanAntlr()
        exit()
    else:
        print("no such file")
        exit()


def compileAntlr():
    subprocess.Popen(["antlr4","-visitor","Unidades.g4"]).wait()

def compileJava():
    subprocess.Popen("javac Unidades*.java",shell=True).wait()

def exeJava(filepath):
    subprocess.Popen(["java","UnidadesMain",filepath]).wait()

def cleanAntlr():
    subprocess.Popen(["antlr4-clean"],stdout=subprocess.DEVNULL).wait()    

main()