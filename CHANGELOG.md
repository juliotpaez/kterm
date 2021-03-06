# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.3.0-beta] - 2020-01-29

### Add

- Option to log a logger with a default log level.

### Change

- Change constructors to define a default log level.

## [0.2.1-beta] - 2019-12-14

### Change

- Make Logger constructors public to allow to map an exception.

## [0.2.0-beta] - 2019-09-30

### Add

- Logger as cause of another logger.

### Change

- All the code refactored.

## [0.1.8-beta] - 2019-08-31

### :bug: Fix

- The builder function of the loggers now is called just once.

## [0.1.7-beta] - 2019-07-21

### Add

- Option for to show newline characters.

### Change

- Simplify source code printing.

### :bug: Fix

- Representations of multiline source code elements.

## [0.1.6-beta] - **SKIPPED**

## [0.1.5-beta] - 2019-07-11

### Add

- Option for source code sections to print the message inline (default) or at the bottom.

### Changes

- Now multiline comments can have inline messages.

### :bug: Fix

- Selection of source code printer now correctly change between Coloring and Erasing.

## [0.1.4-beta] - 2019-07-10

### Change

- Multiline code hints are now highlighted by default using colors instead of erasing the rest of the characters.
- An option is added to highlight code hints using the erasing method.

## [0.1.3-beta] - 2019-06-15

### Fix

- Issue in exception tree mapper when the filename is null.

## [0.1.2-beta] - 2019-06-08

### Add

- `build` method to `Logger` to create it but logging lately. 

### Change

- :art: Format of the code.
- Documentation

## [0.1.1-beta] - 2019-04-21

### Add

- **Documentation**: in `html` and `markdown` format.

### Change

- **README**: updated documentation with some logging examples.
- **README**: updated documentation with some prompt examples.
- Now the `confirmation` and `menu` prompts are fully defined with a builder.

### Fix

- Bad representation for multiline messages in stack trace, source code title and source code message.
  
## [0.1.0] - 2019-04-20

### Add

- **Files**: [license](./LICENSE), [changelog](#changelog) and [readme](./README.md)
- **Logger** for terminal
- **Prompts**
  - **Menu** by tags
  - **Confirm**
