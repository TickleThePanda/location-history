PRODUCT := location-history

BINDIR := bin
SRCDIR := src
OBJDIR := obj

srcdir = src/
bindir = bin/

CC = gcc -O
CDEBUG = -g
CFLAGS = $(CDEBUG) -I $(srcdir)

SRCFILES := $(wildcard $(SRCDIR)/*.c) 
OBJFILES := $(patsubst $(SRCDIR)/%.c,$(OBJDIR)/%.o,$(SRCFILES)) 

$(BINDIR)/$(PRODUCT): $(OBJFILES) 
	$(CC) $(CFLAGS) $^ -o $@ 

$(OBJDIR)/%.o: $(SRCDIR)/%.c
	$(CC) $(CFLAGS) -c $< -o $@ 

.PHONY: clean
clean:
	rm $(OBJDIR)/* $(BINDIR)/$(PRODUCT)

