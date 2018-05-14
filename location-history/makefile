PRODUCT := location-history

BINDIR := bin
SRCDIR := src
OBJDIR := obj

srcdir = src/
bindir = bin/

CC = gcc -O
CDEBUG = -g
CFLAGS = $(CDEBUG) -I $(srcdir)
LINKER = -lm

SRCFILES := $(wildcard $(SRCDIR)/*.c) 
OBJFILES := $(patsubst $(SRCDIR)/%.c,$(OBJDIR)/%.o,$(SRCFILES)) 

$(BINDIR)/$(PRODUCT): $(OBJFILES) 
	$(CC) $(CFLAGS) $^ -o $@ $(LINKER)

$(OBJDIR)/%.o: $(SRCDIR)/%.c
	$(CC) $(CFLAGS) -c $< -o $@ $(LINKER)

.PHONY: clean
clean:
	rm $(OBJDIR)/* $(BINDIR)/$(PRODUCT)

