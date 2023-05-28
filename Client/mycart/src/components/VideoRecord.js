import React, { useState } from 'react';
import VideoRecorder from 'react-video-recorder'
import ReactPlayer from 'react-player'
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import VideoPlayer from 'react-video-js-player';

function VideoRecord(props) {
  const [vblod, setVblob] = useState();
  const player = {};
  const onupload = (value, path) => {
    console.log(value);
    let d = { video: value };
    APIService.postRequestFile('/import/save-video', d).then(data => {
      let src = APP_URL + "/import/video?path=mycart/recod.webm";
      setVblob(src);
    });
  }

  const onPlayerReady = (player) => {
    player = player;
  }

  return (<div>
    <VideoRecorder onStartRecording={() => {
    //  setVblob(APP_URL);
    }}
      onRecordingComplete={(videoBlob) => {
        const fileReader = new FileReader();
        fileReader.onload = (e) => {
          onupload(e.target.result);
        };
        fileReader.readAsDataURL(videoBlob);


      }}
    />
    {vblod && <VideoPlayer
      controls={true}
      src={vblod}
      width="350"
      height="350"
      onReady={onPlayerReady.bind(this)} />}

  </div>

  );

}

export default VideoRecord;