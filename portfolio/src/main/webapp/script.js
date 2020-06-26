// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

window.onload = function onLoad() {
  addListenersToButtons();
  showFirstTabContent();
}

function addListenersToButtons() {
  const expandableContentButtons = document.getElementsByClassName("expandable");

  // Add an event listener for click to each button.
  for (let buttonNumber = 0; buttonNumber < expandableContentButtons.length; buttonNumber++) {
    expandableContentButtons[buttonNumber].addEventListener("click", toggleContentVisibility);
  }
}

function showFirstTabContent(){
  const tabContent = document.getElementsByClassName("tab-content");
 
  //Make the first tab shown.
  showContentForTab(tabContent[0].id, "peachpuff");
}

window.onscroll = changeNavbarStickiness;

/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
  const randomFacts =
      ['I like to peel cuties in a way that keeps the peel in one piece.', 
          'I really like kiwis.', 'When I was younger, I wanted to be an astronaut.', 
              'My favorite movie is Despicable Me 2.'];

  // Pick a random fact.
  const fact = randomFacts[Math.floor(Math.random() * randomFacts.length)];

  // Add it to the page.
  const randomFactContainer = document.getElementById('random-fact-container');
  randomFactContainer.innerText = fact;
}

/* 
 * Expands content when content title button is clicked.
 */
function toggleContentVisibility(){
  const content = this.nextElementSibling;

  // Toggle the visibility of the content.
  if (content.style.display === "block") {
    content.style.display = "none";
  } else {
    content.style.display = "block";
  }

  // Toggle whether plus or minus is shown on the button.
  this.classList.toggle("active");
}

/*
 * Show project content that corresponds to selected tab
 * and set the tab color to the content background color.
 */
function showContentForTab(selectedProjectName, activeTabColor) {
  const tabContent = document.getElementsByClassName("tab-content");
  const tabButtons = document.getElementsByClassName("select-tab");
    
  // Make the only the tab content with id the same as the selected 
  // project name visible and make only the corresponding tab match 
  // with the content background color.
  for (let i = 0; i < tabContent.length; i++) {
    if (tabContent[i].id === selectedProjectName) {
      tabContent[i].style.display = "inline-block";
      tabButtons[i].style.backgroundColor = activeTabColor;
    } else {
      tabContent[i].style.display = "none";
      tabButtons[i].style.backgroundColor = "aliceblue";
    }
  }
}

/**
 * Makes navigation bar become fixed at top when it is about to go out of view. When it is in view, 
 * it is changed to being shown like all other components of the page.
 */
function changeNavbarStickiness() {
  const navbar = document.getElementById("navbar");
  const navbarThickness = navbar.offsetTop;

  // Change navbar CSS classlist based on whether navbar is in view or out of view
  if (window.pageYOffset >= navbarThickness) {
    navbar.classList.add("sticky");
  } else {
    navbar.classList.remove("sticky");
  }
}

/*
 * Get the greeting by fetching data from the /data endpoint and display
 * the greeting. 
 */
function getGreeting() {
  fetch('/data').then(response => response.text()).then((greeting) => {
    document.getElementById('greeting-container').innerText = greeting;
  });
}