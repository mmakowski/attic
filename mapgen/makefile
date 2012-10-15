CC=gcc
CFLAGS=-g -Wall
ALL=mapgen-test.exe
OUTDIR=bin

all: $(ALL)
 
mapgen-test.exe: mapgen.o mapgen-test.o
	$(CC) -o mapgen-test.exe mapgen.o mapgen-test.o
#-lstdc++ 

clean:
	rm -f $(OUTDIR)/*.o $(OUTDIR)/$(ALL)