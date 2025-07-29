TalentFindr is a job portal website that mimics LinkedIn . It provides role based acess for recruiters and job Seekers using Spring Security .

Functionalities of Recruiters:-
1) post jobs
2) Create a full Recruiter profile .
3) View profiles of candidates applied .
4) Download their Resumes .

Of JobSeekers :-
1) Create a profile
2) Apply and save  jobs .
3) Filter and search for jobs available using date of job posted,location etc .
4) Upload resume .


For the chat feature:

* Uses websockets for live communication between recruiter and jobseeker.
* Uses STOMP(simple text oriented message protocol) over websockets for communication
* The frontend basically starts the websocket connection , while the server waits for json message format, it it to the database and publishes it to all the subscribers in real tine

The databse is hosted on aiven .
 
Tech Stack:- SpringBoot, MySql, Bootstrap CSS ,Websocket/SockJs .
