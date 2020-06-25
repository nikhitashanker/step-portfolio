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

window.onscroll = changeNavbarStickiness;

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