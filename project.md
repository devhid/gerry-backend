<h1 align=center>Project Specification</h1>
<p align=center><strong>Team Name</strong>: Jazz</p>
<p align=center><strong>Team Members</strong>: Mankirat Gulati, Andy Liang, Stanley Lim, Johnny So</p>
<p align=center>States: <strong>California</strong>, <strong>Virginia</strong>, and <strong>Utah</strong></p>

## Background
* A Gerrymander is a voting district that is designed to serve some political purpose. The name refers to both a salamander and Eldridge Gerry, whose newly created voting district about 200 years ago was said to resemble a salamander. 

* Within the past 10 years, databases for voter characterization as well as tools for precise map generation have made it possible to create congressional districts that favor the party responsible for the creation of the districts. Redistricting is done in states where census data requires a change in the number of delegates in the state, and the 2010 census triggered redistricting in a number of states. Many of these redistricting efforts resulted in a shift in the political representation in the states. 

* As the realization of the impact of these changes has grown, various technical approaches to the issue have been proposed, some as quantitative measures of the presence of Gerrymandering, others as legal challenges to redistricting, and yet others as draft bills in Congress to minimize the effect of future redistricting. Many of the redistricting changes following the 2010 census used provisions of the Voting Rights Act (VRA) in way not intended by the people responsible for the VRA. For example, the VRA provided for majority-minority districts, which were intended as a means to ensure representation of minority groups in Congress. However, the VRA was used to "pack" districts, which promoted Gerrymandered districts, and also fewer majority-minority districts than might otherwise have been possible.

* The system to be developed in this project will allow for the generation of congressional district boundaries without political influence but with the maximum number of majority-minority districts. This system relates to, but is different from, systems developed in previous sections of CSE308. Those systems gathered data associated with this topic, analyzed the data using many of the proposed measures of Gerrymandering, presented the data in a way that highlighted the effects of gerrymandering, and explored various algorithms for automatic redistricting.

## System Components

### 1. GUI
  * The GUI will allow users to select a state to analyze and display actual congressional districts in a map of the selected state, while presenting data associated with congressional districting (e.g., measures of the effect of gerrymandering). 
  * State maps should also include display of precincts. For performance reasons, all precincts in a state are not required to be displayed at the initial time the state is displayed. 
  * Data concerning the solution should be visible to the user, real-time.

### 2. Database
  * Data used by the system should be contained in a 3-state database. 
  * At a minimum, this data includes:
    1. Congressional election results for the two most recent elections (2016 and 2018).
    2. Presidential election results for 2016. 
    3. Geospatial Data Describing: the boundary of each state, congressional district, interim district (aka, cluster), and voting precinct.
    4. State constitution guidelines for redistricting. 
   * For **#4**, this will include pure text of the related sections in the state constitution as well as a concise summary of those requirements. Teams may choose to represent data as collections of precinct data for the state and data in the form of a connected graph of precincts.

### 3. District Generation
  * Your system will include a 2-step algorithmic approach to automated district generation. 
    1. The first step will use a graph partition algorithm that will generate an initial set of congressional districts, the number of which is specified by the user in the GUI. 
    2. The second step refines the initial set of districts using simulated annealing. 
  * In both phases of the process, the solution goal will be to generate the maximum number of majority-minority congressional districts, while adhering to constraints and objectives specified by the user (e.g., district compactness).
### 4. Objective Function
  * You will measure the quality of each interim and final round of districting through the use of an objective function. 
  *  The objective function will include terms (e.g., Polsby-Popper compactness) whose values are normalized so each term equally contributes to the function value. 
  * Each term will also have a weight that will enable the user to adjust the relative importance of that term.

### 5. Preprocessing
  * This sub-system will generate the graph representing the precincts (nodes) and the edges that show contiguous precincts.
    * **Nodes** should include census demographic data as well as election data. 
    * **Edges** should include information on the joinability of precincts. 
  * Your preprocessing should include some way of manually adjusting contiguity data in the event of geographic anomalies in the precinct boundary data.

## Goals or Constraints
The generation process of the system should combine precincts so that the resulting districts adhere to the goals or constraints. At a minimum, this will include:

### 1. Compactness
  * System will include a variety of measures of compactness, at least one being graph-theoretic compactness.
  
### 2. Contiugity
  * Precincts in a congressional district should be geographically connected by sharing a common boundary of some distance as specified in the GUI. 
  * The system will include a manual override to provide connectivity for precincts that are logically connected, but not able to be found automatically (e.g., geo data anomalies, precincts separated by a natural boundary such as a river, etc.).
### 3. Equal Population
  * Congressional districts will be of approximately equal population.
  
### 4. Partisian Fairness
  * The system will include multiple measures of political fairness (e.g., efficiency gap).
  
### 5. Racial/Ethnic Considerations
  * The system will include a feature for majority-minority districts in which the user specifies 
    - the number of such districts, 
    - the groups considered in the calculation (e.g., African-American), and 
    - the maximum and minimum vote percentage for the group. 
  * Your system will also summarize data indicating whether the minority group is politically cohesive. 
  * Using precinct demographic data and precinct voting data, summarize the voting patterns of each minority group.
  
## System Requirements

### Server
 * Server-side code should be written in **Java**.
 * Should handle concurrent users.
 
### Database
 1. Must be on a remote machine.
 2. Must be SQL-based.

### GUI
 * Should implement at least **40 use cases**.
 * Can use any web technologies and frameworks to develop the GUI and abstract the interactions.
 
### GUI / Server Interaction
 * If you use **Spring**, you are responsible for **understanding how that automation is performed during your design and code reviews**.
 
### Object to Relational Mapping
 * You should implement a clear separation between your server code and your DB. 
 * You may develop the mapping code yourself, and use **JDBC** for the interface to the DB or you can use a standard mapping approach (e.g., **JPA / Hibernate**).
 
### Data Gathering / DB Loading
 * You can use any language or library that is appropriate for the extraction of data. 
 * This largely depends on the data you locate. 
 * If you find available downloads in XML or JSON format, there are libraries that will automate much of the work. 
 * In past projects, Python has proven to be useful.

## Due Dates for Deliverables

<table>
  <tr>
    <thead>
      <th>Due Date</td>
      <th>Deliverable</td>
      <th>Delivery Mode</td>
    </thead>
  </tr>
  <tbody>
    <tr>
      <td>9/29</td>
      <td>
        1. <strong>Use Cases</strong> (title and description) - <strong>PDF</strong><br>
        2. <strong>GUI State Page</strong> (electronically generated or hand-drawn) - <strong>PNG or JPEG</strong><br>
        3. <strong>README</strong> (optional) - <strong>PDF</strong>
      </td>
      <td>
        Dropbox sub-folder named <strong>Sep29</strong>.
      </td>
    </tr>
    <tr>
      <td>10/13</td>
     <td>
       <strong>A user interface that will demonstrate your proposed GUI.</strong><br><br>
       - This is not a complete working interface, just a sequence of pages to demonstrate the functionality (but the page-to-page links should work).<br>
       - Each page should be consistent in design and all forms, GUI components and sample text should be included.</td>
      <td>
       Dropbox sub-folder named <strong>Oct13</strong>.
      </td>
    </tr>
    <tr>
      <td>10/20</td>
      <td>
       1. <strong>Object Design</strong> (initial class diagram) - <strong>PDF, JPEG, PNG</strong><br>
          - The initial class diagram should contain most of your classes (you may not be able to identify all the non-domain classes yet), and each of these classes should contain all the instance variables and many of the methods.<br><br>
       2. <strong>Representative Sequence Diagram</strong> - <strong>PDF, JPEG, PNG</strong><br>
          - The purpose of this item is for you to obtain feedback on your approach to sequence diagrams.<br>
          - The feedback should be helpful when you prepare for your design review.</td>
      <td>
       Dropbox sub-folder named <strong>Oct20</strong>.
      </td>
    </tr>
    <tr>
      <td>10/17-11/5</td>
      <td>
       <strong>Design Review</strong><br><br>
       - You will conduct a brief design review of your design.<br> 
       - Many of these reviews will be conducted in class, while the remainder will be conducted outside of the class during the later part of the time period.<br> 
       - You should be prepared to <strong>present your overall design</strong>, primarily <strong>through</strong> your <strong>GUI, use cases, sequence diagrams, class diagram, and preliminary DB design</strong>.<br> 
       - You should either have your material on a flash memory (preferred) or on your own laptop, which we will connect to the projector.<br>
       - You should have all your sequence diagrams, class diagrams, and use cases available to respond to questions. <br>
       - Your design grade will be based primarily on this review.<br>
       - The review will also contribute to your oral communications grade, although you have more potential for credits if you present in class (instead of the separate session).<br>
       - The grading criteria for your design is primarily the completeness and quality of the material. <br>
       - Remember, maintainability is considered an important part of quality. You may use the suggestions in your design review to modify your final design.<br>
       - All members should be active in the design review.
      </td>
      <td>Grading Rubric</td>
    </tr>
    <tr>
      <td>11/17</td>
     <td>
      <strong>Final Design Document</strong><br><br>
      - This completes your design document, includes the database design, and all non-domain objects.<br> 
      - <strong>Note</strong>: The preferred documentation of your DB design is a <strong>Relational Data Model (RDM)</strong>, not an ER diagram.<br>
      - You should be sure that your DB is <strong>properly normalized</strong> (up to <strong>3rd normal form</strong>).<br>
      - The final design can include any changes to your preliminary design, especially changes resulting from your design review.
      </td>
      <td>
        Dropbox sub-folder named <strong>Nov17</strong>.
      </td>
    </tr>
    <tr>
     <td>11/24</td>
     <td>
      <strong>Complete Initial Build</strong><br><br>
      - This initial build should include enough functioning use cases for minimal user access.<br> 
      - Typically this will include login, logout, and a few data display use cases.<br> 
      - Integration with your database is not required at this time, so you can stub your persistence layer with hard-coded values.<br>
      - This build should include all your GUI pages and the majority of your domain objects, although all the methods in your classes can be stubbed.<br>
      - This code should work to the extent that your pages will transition properly.
     </td>
     <td>
      Video (5 minutes) showing GUI and code in Dropbox sub-folder named <strong>Nov24</strong>.
     </td>
    </tr>
    <tr>
     <td>11/24</td>
     <td>
      <strong>Test Plan Document</strong><br><br>
      - This document should describe your high level testing plan for your system.<br> 
      - Describe how you will change initial conditions to run various test scenarios.<br> 
      - Describe how you intend to compare results of various tests.<br>
      - Describe how you expect to gather data to improve your algorithms.<br> 
      - <strong>The test plan should be 1-2 pages.</strong>
     </td>
     <td>Dropbox sub-folder named <strong>Nov24</strong>.</td>
    </tr>
    <tr>
      <td>11/18-11/26</td>
      <td>
       <strong>Code Review</strong><br><br>
       - Some code walk-throughs will be held in class, but most will be held outside of class.<br> 
       - You are expected to start your session with the code associated with a typical use case, including controller classes, domain classes, utility classes, database, persistence layer, and client-side GUI code.<br> 
       - Be prepared with all your code since you will likely be asked to show code modules other than the one you prepared.<br>
       - The code should adhere to the coding conventions, be written in a way that enables the reviewer to understand the code, and should also be formatted to enhance readability.<br> 
       - The code will not only include use case code, but also testing code, especially code used to monitor system performance.<br> 
       - You should also have your design material and user interface available to respond to questions.<br> 
       - This part of your project will be graded as a component of your final project grade.<br>
       - You can refer to a <a href="https://www3.cs.stonybrook.edu/~cse308/Section01/CodeReviewCategories.html">typical checklist of code issues</a> to be addressed.
      </td>
      <td></td>
    </tr>
    <tr>
      <td>12/1</td>
      <td>
       <strong>Build (with DB)</strong><br><br>
       - This build should have a minimum of half of your use cases.
      </td>
      <td>
        Video (5 minutes) showing GUI (2 minutes) and code in Dropbox sub-folder named <strong>Dec1</strong>.
      </td>
    </tr>
    <tr>
      <td>12/5-12/17</td>
     <td>
      1. <strong>Formal Demonstration of Project</strong><br> 
      - This part of your project will be graded as a component of your final project grade.<br><br>
      2. <strong>Final Specification Document, Design Document, Test Plan, and Source Code</strong><br> 
      - Provide any updates you have made to material previously submitted.</td>
      <td>Bring mapping of our use cases to numbered use cases in master list.</td>
    </tr>
    <tr>
      <td>Demo</td>
     <td><strong>Final Build</strong><br><br>
      - This build should contain working code for all the use cases presented in your project demonstration.
      </td>
      <td></td>
    </tr>
  </tbody>
</table>
