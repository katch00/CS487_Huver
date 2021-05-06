function getDrivers(city, numPass) {
  fetch(`/getDrivers?city=${city}&numPass=${numPass}`).then(response => response.json()).then((drivers) => {
    const driverListEl = document.getElementById('driverList');
    drivers.forEach((driver) => {
        driverListEl.appendChild(createDriverElement(driver));
    })
  });

}

async function checkUser(){
    const username = await fetch('/sessionLogin', {method: 'GET'}).then(response => response.json()).then((data)=> {result = data; console.log(result); return result});
    console.log(username + "kalp2");
    return username;
}

function sendRequest(name, cost) {
    fetch(`/requestDriver?driverName=${name}&cost=${cost}`);
}

function createDriverElement(driver) {
  const driverElement = document.createElement('li');
  driverElement.className = 'driver';

  const nameElement = document.createElement('span');
  const time = "15";
  const cost = "30";
  nameElement.innerText = 'Name: ' + driver.firstName + "  Expected wait time:" + time + "  Total Cost:" + cost;
  
  const breakEl = document.createElement('br');
  const selectButtonElement = document.createElement('button');
  selectButtonElement.innerText = 'Select Me!';
  selectButtonElement.className = 'button';
  selectButtonElement.style = 'background-color: purple; color: white;';
  //selectButtonElement.onclick = `sendRequest(${driver.firstName}, ${cost})`;

  driverElement.appendChild(nameElement);
  driverElement.appendChild(breakEl);
  driverElement.appendChild(selectButtonElement);
  return driverElement;
}

function createCost(time){
  var cost = (1.5 + Math.random()) * time;
  console.log(cost);
  return cost;
}

function createTime(min, max){
  var time = Math.random() * min + (max-min);
  console.log(time);
  return time;
}