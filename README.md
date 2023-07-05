# Project 1.5 - We Have Tables Now

I thought a lot about what kind of data structure would hold an index, and stumbled into the idea of a table.
After some research, I found Google's devs already implemented a table collection, and challenged myself to create a
very simple implementation of Guava's TreeBasedTable. 2 weeks and over 1000 lines of code later, I decided I didn't have the ability
required to reproduce something made by a team of Google developers, and I just decided to import it instead.

I learned a ton from it though, and it led to this project being born.

## Changes:
SimpleInvertedIndex is deprecated. All shall be tables.

A new set of modules have been added: JsonWriteableTable, AbstractStemAndFileTable, SimpleInvertedIndexTable,
and JsonTableWriter.

#CRC Model:
The exact same as Project 1 - it's the implementations, not the interfaces or how they collaborate with each other, that are different.
I'll put it [here](https://lucid.app/lucidchart/7fe9eb7c-befc-41dc-a8d5-ffd00daeb6d8/edit?invitationId=inv_3850d397-fcf1-4a9d-9ffd-25499cddf566&page=0_0#)
for reference.