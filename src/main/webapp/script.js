function getDrivers(city, numPass) {
  fetch(`/getDrivers?city=${city}&numPass=${numPass}`).then(response => response.json()).then((drivers) => {
    const driverListEl = document.getElementById('driverList');
    drivers.forEach((driver) => {
        driverListEl.appendChild(createDriverElement(driver));
    })
  });

}

function createDriverElement(driver) {
  const driverElement = document.createElement('li');
  driverElement.className = 'driver';

  const nameElement = document.createElement('span');
  nameElement.innerText = 'Name: ' + driver.firstName;

  const breakEl = document.createElement('br');
  const selectButtonElement = document.createElement('button');
  selectButtonElement.innerText = 'Select Me!';
  selectButtonElement.className = 'button';
  selectButtonElement.style = 'background-color: purple; color: white;';

  driverElement.appendChild(nameElement);
  driverElement.appendChild(breakEl);
  driverElement.appendChild(selectButtonElement);
  return driverElement;
}