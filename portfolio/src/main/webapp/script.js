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

window.onload = function addEventListenerForClick() {
    const expandableContentButtons = document.getElementsByClassName("expandable");

    // Add an event listener for click to each button.
    for (let buttonNumber = 0; buttonNumber < expandableContentButtons.length; buttonNumber++) {
        expandableContentButtons[buttonNumber].addEventListener("click", toggleContentVisibility);
    }
}

/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
  const randomFacts =
      ['I like to peel cuties in a way that keeps the peel in one piece.', 'I know how to speak Kannada.', 'When I was younger, I wanted to be an astronaut.', 'My favorite movie is Despicable Me 2.'];

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
}

/*
  * Show project content that corresponds to selected tab.
*/
function showContentForTab(selectedProjectName) {
    const tabContent = document.getElementsByClassName("tab-content");
    
    // Make the only the tab content with id the same as the selected project name visible.
    for (let i = 0; i < tabContent.length; i++) {
        if (tabContent[i].id === selectedProjectName) {
            tabContent[i].style.display = "inline-block";
        } else {
            tabContent[i].style.display = "none";
        }
    }
}