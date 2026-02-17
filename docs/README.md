# Darwin's User Guide

---
Meet **Darwin**, a friendly chatbot meant to keep track of your tasks! It’s
- text-based
- easy to learn
- _SUPER FAST_ to use! 

---
## Quick Start
1. Ensure you have Java `17` or above installed in your Computer.<br>
**Mac users:** Ensure you have the precise JDK version prescribed 
[here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here]().

3. Copy the file to the folder you want to use as the home folder for your Darwin chatbot.

4. Open a command terminal, switch the directory (`cd`) into the folder you put the jar file in (eg. `cd Downloads`), and use the 
`java -jar darwin.jar` command to run the application.<br>
A GUI similar to the below should appear in a few seconds.

![Darwin interface](Ui.png)

---
## Features
- List tasks
- Add tasks (Todos, Deadlines, Events)
- Mark / Unmark tasks as completed 
- Delete tasks 
- Find tasks by keyword

---
### Viewing all tasks: `list`
Shows a list of all tasks in your task tracker.<br>
Format: `list`

---
### Adding a todo task: `todo`
Adds a todo task to your task list.<br>
Format: `todo DESCRIPTION`<br>
Example: 
- `todo Buy book`

---
### Adding a event task: `event`
Adds an event task with a start and end time to your task list.
Format: `event DESCRIPTION /from START /to END`<br>
- End date cannot be earlier than from date.
- Dates should be in the format YYYY-MM-DD.<br>

Example:
- `event Book fair /from 2026-01-01 /to 2026-01-31`

---
### Adding a deadline task: `deadline`
Adds a task with a deadline to your task list.<br>
Format: `deadline DESCRIPTION /by DATE`<br>
- Dates should be in the format YYYY-MM-DD.<br>

Example:
- `deadline Return book /by 2026-01-31`

---
### Marking a task as done: `mark`
Marks the specified task as completed.<br>
Format: `mark INDEX`<br>
- Marks the task at the specified `INDEX` as done.
- The index refers to the index number shown in the displayed task list.
- The index **must be a positive integer** 1, 2, 3, … and within size of task list.

Example: 
- `mark 2` marks the 2nd task in the list as done.

---
### Unmarking a task: `unmark`
Marks the specified task as not done.<br>
Format: `unmark INDEX`<br>
- Marks the task at the specified `INDEX` as not done.
- The index refers to the index number shown in the displayed task list.
- The index **must be a positive integer** 1, 2, 3, … and within size of task list.

Example:
- `unmark 2` marks the 2nd task in the list as not done.

---
### Deleting a task: `delete`
Deletes the specified task from your task list.<br>
Format: `delete INDEX`<br>
- Deletes the task at the specified `INDEX`.
- The index refers to the index number shown in the displayed task list.
- The index **must be a positive integer** 1, 2, 3, … and within size of task list.

Example:
- `delete 3` deletes the 3rd task in the list.

---
### Finding tasks by keyword: `find`
Finds tasks whose descriptions contain the given keyword.<br>
Format: `find KEYWORD`<br>
- Keyword is case-insensitive e.g. `BOOK` will match to `book`
- All tasks containing the keyword will be shown
  e.g. `book` will show results containing `book` and `books`

Example: 
- `find book` returns all tasks containing “book” in their description.

---
### Exiting the program: `bye`
Exits the application after a short delay.<br>
Format: `bye`<br>

---
### Summary of all functions

|Action|Format|Examples|
|------|------|--------|
|Viewing all tasks|`list`|`list`|
|Adding a todo task|`todo DESCRIPTION`|`todo Buy book`|
|Adding a event task|`event DESCRIPTION /from START /to END`|`event Book fair /from 2026-01-01 /to 2026-01-31`|
|Adding a deadline task|`deadline DESCRIPTION /by DATE`|`deadline Return book /by 2026-01-31`|
|Marking a task as done|`mark INDEX`|`mark 2`|
|Unmarking a task|`unmark INDEX`|`unmark 2`|
|Deleting a task|`delete INDEX`|`delete 3`|
|Finding tasks by keyword|`find KEYWORD`|`find book`|
|Exiting the program|`bye`|`bye`|