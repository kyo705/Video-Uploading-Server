# Video Uploading Server

```
VOD 서비스에서 유저의 파일 업로드를 처리하는 서버
```


## 기술 스택


> - **protocol** : http, tus
> - **language** : java
> - **framework** : springboot, spring mvc
> - **message queue** : kafka
> - **external library** : spring kafka, tus-java-server(https://github.com/tomdesair/tus-java-server)

## Upload Server API
[***썸네일 파일 업로드 API***](https://kyo705.github.io/Video-Uploading-Server/src/docs/asciidoc/thumbnail_upload.html)   

[***동영상 파일 업로드 API***]()


## Tus Protocol이란?

> http 위에서 정의된 대용량 파일 업로드를 위한 프로토콜   
> tus protocol 공식 문서 : https://tus.io/protocols/resumable-upload

### 대용량 파일 업로드시 tus protocol이 필요한 이유

> **기존 웹 환경에서의 대용량 파일 업로드시 문제점**   
>
> 1. http로 **대용량 파일**을 한 번에 업로드 시 서버의 할당된 메모리를 초과되는 문제 발생
>
>
> 2. 파일의 업로드 과정 중 장애로 인해 중단되면 다시 처음부터 업로드를 시작해야 함
>
> **TUS Protocol를 통한 해결 방안**
>
> 1. 파일 업로드 진행 시 Upload-Offset 해더 값을 통해 어디까지 파일 데이터를 저장했는지 파악함   
> => 응답된 오프셋값을 통해 파일을 부분적으로 여러 번 데이터를 전송 가능
>
>
> 2. 초기 프로토콜 연결 시 업로드할 대용량 파일 데이터가 얼마나 저장되어 있는지 파악함   
> => 업로드할 파일의 저장되지 않은 부분부터 업로드 가능


### Tus protocol API

***초기 설정시 HEAD 메소드 사용***
```
Request:

HEAD /files/24e533e02ec3bc40c387f1a0e460e216 HTTP/1.1
Host: tus.example.org
Tus-Resumable: 1.0.0
```
***200 OK 시 기존 저장된 일부 데이터 존재***
```
Response:

HTTP/1.1 200 OK
Upload-Offset: 70
Tus-Resumable: 1.0.0
```
***404 NOT FOUND 시 기존 데이터 존재하지 않음***
```
Response:

HTTP/1.1 404 NOT FOUND
Cache-Control: no-store
Tus-Resumable: 1.0.0
```
------------------------

***기존 데이터 존재하지 않을 시 POST를 통해 파일 메타데이터 생성***

```
Request:

POST /files HTTP/1.1
Host: tus.example.org
Content-Length: 0
Upload-Length: 100
Tus-Resumable: 1.0.0
Upload-Metadata: filename d29ybGRfZG9taW5hdGlvbl9wbGFuLnBkZg==,is_confidential
```
***응답***
```
Response:

HTTP/1.1 201 Created
Location: https://tus.example.org/files/24e533e02ec3bc40c387f1a0e460e216
Tus-Resumable: 1.0.0
```

-------------------------
***청크 단위로 파일 업로드시 PATCH 메소드 사용***
```
Request:

PATCH /files/24e533e02ec3bc40c387f1a0e460e216 HTTP/1.1
Host: tus.example.org
Content-Type: application/offset+octet-stream
Content-Length: 30
Upload-Offset: 70
Tus-Resumable: 1.0.0
```
***응답시 저장한 파일의 offset 값을 헤더로 알려줌***
```
Response:

HTTP/1.1 204 No Content
Tus-Resumable: 1.0.0
Upload-Offset: 100
```

##### 이 외에도 다양한 헤더값(OPTION, DELETE)이 정의되어 있음. 자세한 내용은 공식 문서 참조바람
