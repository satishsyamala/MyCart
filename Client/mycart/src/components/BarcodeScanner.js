import React from 'react';
import BarcodeScannerComponent from "react-webcam-barcode-scanner";

function BarcodeScanner(props) {

  const [data, setData] = React.useState('Not Found');

  const onScan = (result) => {
    console.log(JSON.stringify(result));
    if (result) {
      props.onScanner(result.text);
    }
  }

  return (
    <>
      <BarcodeScannerComponent
      
        onUpdate={(err, result) => onScan(result)}
      />
    
    </>
  )
}

export default BarcodeScanner;