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
  const time = createTime(7, 32).toFixed(2);
  const cost = createCost(time).toFixed(2);
  nameElement.innerText = 'Name: ' + driver.firstName + ".  Expected wait time: " + time + " mins.  Total Cost: $" + cost;
  
  const breakEl = document.createElement('br');
  const selectButtonElement = document.createElement('button');
  selectButtonElement.innerText = 'Select Me!';
  selectButtonElement.className = 'button';
  selectButtonElement.style = 'background-color: purple; color: white;';
  selectButtonElement.onclick = 
    function () {
      console.log(driver.firstName);
      fetch(`/requestDriver?driverName=${driver.firstName}&cost=${cost}&time=${time}`, {method: 'POST'})
      .then(response => {
          console.log(response);
        if (response.redirected) {
            window.location.href = response.url;
        }
      })
      
  }

  driverElement.appendChild(nameElement);
  driverElement.appendChild(breakEl);
  driverElement.appendChild(selectButtonElement);
  return driverElement;
}

function setRequest(cust, cost, time)
{
    var customerLabel = document.getElementById('customer');
    customerLabel.innerText = cust;
    var timeLabel = document.getElementById('time');
    timeLabel.innerText = time + ' mins';
    var costLabel = document.getElementById('pay');
    costLabel.innerText = ' $' + cost;

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