

<img width="1077" height="424" alt="flow_chart" src="https://github.com/user-attachments/assets/b6e615de-8473-4c12-a850-d46c1f5d4a91" />







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
* The frontend basically starts the websocket connection , while the server waits for json message format, it saves it to the database and publishes it to all the subscribers in real tine

Demo:-https://drive.google.com/drive/u/1/folders/1wOopcXLyX_xaZTgnxGphzguHGxTIIQbI

The databse is hosted on aiven .

The website is currently hosted on AWS on an EC2 instance and is attached to a S3 bucket for image/resume storage .
 
Tech Stack:- SpringBoot, MySql, Bootstrap CSS ,Websocket/SockJs .
