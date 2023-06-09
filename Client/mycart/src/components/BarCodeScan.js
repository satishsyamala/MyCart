import React from 'react';
import BarcodeScannerComponent from "react-webcam-barcode-scanner";
 
function BarCodeScan() {
 
  const [ data, setData ] = React.useState('Not Found');
 
  return (
    <>
      <BarcodeScannerComponent
        width={500}
        height={500}
        onUpdate={(err, result) => {
          if (result) setData(result.text)
          else setData('Not Found')
        }}
      />
      <p>{data}</p>
    </>
  )
}
 
export default BarCodeScan;