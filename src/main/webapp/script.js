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

function getFavorites(username) {
  fetch(`/getFavorites?username=${username}`).then(response => response.json()).then((favs) => {
    const favListEl = document.getElementById('favList');
    favs.forEach((fav) => {
        favListEl.appendChild(createfavElement(fav));
    })
  });

}

function createfavElement(fav) {
  const favElement = document.createElement('li');
  favElement.className = 'favorite';

  const nameElement = document.createElement('span');
  nameElement.innerText = 'Address: ' + fav.address;

  const cityElement = document.createElement('span');
  cityElement.innerText = 'City: ' + fav.city;

  const zipElement = document.createElement('span');
  zipElement.innerText = 'Zip Code: ' + fav.zipCode;

  const stateElement = document.createElement('span');
  stateElement.innerText = 'State: ' + fav.state;

  const breakEl = document.createElement('br');

  favElement.appendChild(nameElement);
  favElement.appendChild(breakEl);
  favElement.appendChild(cityElement);
  favElement.appendChild(breakEl);
  favElement.appendChild(zipElement);
  favElement.appendChild(breakEl);
  favElement.appendChild(stateElement);
  return favElement;
}