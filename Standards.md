# Communication Standard

## Message definition

| Message Code       | Action Description                                                                                                                                                                                 |
|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **{X}**            | The argument inside the curly braces represents the variable passed in as parameters. Do not include the braces in your socket response, they’re only for demonstration purposes in this document. |
| **INITIALIZE {X}** | Initializing X fitting rooms.                                                                                                                                                                      |
| **FR_REQ**         | Request a fitting room allocation.                                                                                                                                                                 |
| **FR_EXIT**        | Signal that a user is exiting a fitting room.                                                                                                                                                      |
| **WR_EXIT**        | Signal that a user is leaving the waiting room.                                                                                                                                                    |
| **CLIENT**         | Initial client message to indicate a client.|

---

## Central Server messages

| Client’s message | Central Server’s Response |
| :--- | :--- |
| **INITIALIZE {X}** | “INIT_SUCCESS” <br> “INIT_FAILED” |
| **FR_REQ** | If found a vacant fitting room, respond “{Room_ID} {X}”. <br> **Console message:** “Room_ID is allocated for X seconds.” <br><br> Else if there’s seats in the waiting room, respond “WAIT {X}”. <br> **Console message:** “Please wait for {X} seconds.” <br><br> Else respond: “NO” <br> **Console message:** “There’s no rooms available. Sorry for your inconvenience”. |
| **FR_EXIT** | “{Client’s ID} EJECTED” <br> **Console message:** “Thank you for choosing our service” |
| **WR_EXIT** | “{Client’s ID} EJECTED” <br> **Console message:** “Sorry for your inconvenience” |

---

## Fitting Room’s messages

| Central Server’s message  | Fitting Room’s Response                                                                                                                                                              |
|:--------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **FR_REQ {Client’s ID}**  | If found vacant fitting room, respond with “{Room Number} ALLOC”. <br> Else if there’s seats in the waiting room, respond with: “WAIT {X}”. X is in seconds. <br> Else respond: “NO” |
| **FR_EXIT {Client’s ID}** | “{Client’s ID} EJECTED”                                                                                                                                                              |
| **WR_EXIT {Client’s ID}** | “{Client’s ID} EJECTED”                                                                                                                                                              |
| **GET FR**                | “FR {X}”. <br> X is the number of fitting rooms available                                                                                                                            |
| **GET WR**                | “WR {X}”. <br> X is the number of waiting seats available                                                                                                                            |
| **GET NEXT_AVAIL**        | “AVAIL {X}”. <br> X (Seconds) is the earliest time until there’s a vacant fitting room.                                                                                              |
| **SERVER**                | Initial server message to indicate a server.                                                                                                                                         |