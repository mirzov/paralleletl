ParallelETL
===========
A prototype of an Iteratee-based Scala library for multithreaded ETL (extract, transform, load).

Depends on the Iteratee library from Play2 Web framework.

The idea is to have a generic and easily extensible DSL for processing of data flows with branching, transformations and joins. Data flow elements can be anything - strings, string arrays, table rows, etc. Execution of the ETL tasks is parallelized "for free" due to building upon the Iteratee and Futures/Promises libraries.
