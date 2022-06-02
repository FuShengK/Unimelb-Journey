OBJ     = detect.o executing.o quickSort.o utils.o
CC      = gcc
EXE     = detect
CFLAGS  = -Wall
# -O2 -m32

detect:   $(OBJ) Makefile
	$(CC) $(CFLAGS) -o $(EXE) $(OBJ)

clean:
	rm -f $(OBJ) $(EXE)

usage: $(EXE)
	./$(EXE)

detect.o: detect.h Makefile