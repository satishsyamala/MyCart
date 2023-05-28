import React, { useState, Component, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';

function ShowAll() {
  const { getAll } = useIndexedDB('users');
  const [persons, setPersons] = useState();
 
  useEffect(() => {
   
    getAll().then(personsFromDB => setPersons(personsFromDB));
  }, []);
 
  return (
    <div>
      {persons &&
        persons.map(person => (
        <span>hi</span>
      ))
      }
    </div>
  );
} export default ShowAll;