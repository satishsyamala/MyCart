import React from 'react';
import SignaturePad from 'react-signature-canvas'

import styles from './singnaturestyle.css'

function Signature(props) {

  const [trimmedDataURL, setTrimmedDataURL] = React.useState();
  const [sigPad, setSigPad] = React.useState();
  

 const  clear = () => {
    sigPad.clear();
  };
  const  trim = () => {
    setTrimmedDataURL(sigPad.getTrimmedCanvas().toDataURL("image/png"));
  };


  
    return (
      <div >
        <div>
          <SignaturePad  className=""
            penColor="green" onEnd={()=>trim()}
            ref={(ref) => {
                setSigPad(ref);
            }}
          />
        </div>
        <div>
          <button className={styles.buttons} onClick={()=>clear()}>
            Clear
          </button>
          <button className={styles.buttons} onClick={()=>trim()}>
            Trim
          </button>
        </div>
        {trimmedDataURL ? (
          <img className={styles.sigImage} src={trimmedDataURL} />
        ) : null}
      </div>
    );

}

export default Signature;