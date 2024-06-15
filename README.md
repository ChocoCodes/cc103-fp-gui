# HOOPMASTER: Basketball Tournament Management System


Final Project presented for the culminating activity of the course CC103.


## !!! DISCLAIMER !!!

This project is provided as-is, and there is no guarantee that it will remain updated. Potential users are advised that:
- The software may become outdated over time due to changes in technology or dependencies.
- Replication of this project may require updates or modifications to maintain functionality.
- The maintainers may not actively monitor or update this repository.

For any inquries regarding this project, please see [CONTACT](##Contact).


## Description:

Organizing basketball tournaments is a complex and challenging task that requires significant effort from tournament organizers. One of the major challenges faced by organizers is the management of player and team statistics, which is often handled manually and can lead to errors and delays. As such, it can result in discrepancies in tournament records, causing frustration for both organizers and participants. Moreover, the lack of a centralized system for designing tournament formats and scheduling games can lead to confusion and logistical difficulties.

To address the problem, the proponents developed a Tournament Management System using Java Swing GUI. This system will provide tournament administrators and table officials with intuitive tools for modifying player and team stats, designing tournament formats, and scheduling games.


## Project Information

### Proponents
- John Octavio([ChocoCodes](https://github.com/ChocoCodes))
- Raean Tamayo([Tamagomago](https://github.com/Tamagomago))

### Features

#### A. Organizers

- **Tournament Format Management.** Organizers can define and manage tournament formats(single-round robin and single-elimination) to suit the needs of basketball events.
- **Game Scheduling**. The system offers tools for scheduling games, allowing organizers to allocate game times and opponents efficiently according to the chosen tournament format.
- **Team and Player Management.**  Organizers can manage team registrations, player rosters, and related information, ensuring accurate records throughout the tournament.

#### A. Table Officials

- **Match Management**. Officials can access the scheduled matches and select specific ones to manage, allowing them to focus on the relevant games during the tournament.
- **Individual Team and Player Stats Management**. TO's are responsible for encoding team statistics during matches, including scores, rebounds, assists, and other relevant metrics, ensuring accurate records of team performance. In addition, player stats such as points scored, rebounds, and assists, blocks, and steals are recorded, providing detailed insights into individual player performances throughout the tournament.
- **Match and Player Summary Generation**. TO's can view comprehensive reports of completed matches, including final scores and key statistics, enabling them to generate game reports and provide feedback as needed. In addition, TO's have the ability to store match summaries securely within the system, ensuring that essential data and insights are preserved for future reference and analysis

### System Limitations

- **Limited Statistical Recording.** The system records only basic statistics such as points, rebounds, assists, blocks, and steals for each player. It may not support more advanced metrics or customizable statistical categories.
- **Manual File Handling for Incorrect CSV File Formats.** The system lacks automated error handling for cases where files are not in the correct format. Users must manually handle such errors, potentially leading to data corruption or loss if not addressed properly.
- **Dependency on Default Folders.** The system is limited to using default folders for file storage and organization. It cannot create new folders as needed but it does create new files automatically.
- **Scheduling Limitation with Team Deletion.** When the tournament schedule is set and the admin deletes a team, the team will be removed from the teams list but will still appear in the existing schedule, meaning it will still show up in the viewing schedules, unless deleted per se.
- **Tracking Limitations in Tournament Statistics.** The team statistics for each match can be tracked and recorded throughout the tournament, while player statistics are recorded only in general and not on a per-match basis.
- **Deletion of Team Data.** When a team's CSV file is created with its players' information, deleting that team will remove it from the team list CSV but not delete the CSV file containing the team's players' information. However, the team will not appear in the generated tournament schedule if it is deleted before the schedule is created.


## Licenses

**See [LICENSE](LICENSE) for more information regarding the licenses.**


## Contact

If you encounter any bugs or have suggestions for improvements, please feel free to contact us via:
#### Email: 
- [johnrlnd1704@gmail.com](https://mail.google.com/mail/?view=cm&fs=1&to=johnrlnd1704@gmail.com)
- [tamayoraeanchrissean@gmail.com](https://mail.google.com/mail/?view=cm&fs=1&to=tamayoraeanchrissean@gmail.com)
