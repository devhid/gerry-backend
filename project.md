<h1 align=center>Project Specification</h1>
<p align=center><strong>Team Name</strong>: Jazz</p>
<p align=center><strong>Team Members</strong>: Mankirat Gulati, Andy Liang, Stanley Lim, Johnny So</p>

## Background
* A Gerrymander is a voting district that is designed to serve some political purpose. The name refers to both a salamander and Eldridge Gerry, whose newly created voting district about 200 years ago was said to resemble a salamander. 

* Within the past 10 years, databases for voter characterization as well as tools for precise map generation have made it possible to create congressional districts that favor the party responsible for the creation of the districts. Redistricting is done in states where census data requires a change in the number of delegates in the state, and the 2010 census triggered redistricting in a number of states. Many of these redistricting efforts resulted in a shift in the political representation in the states. 

* As the realization of the impact of these changes has grown, various technical approaches to the issue have been proposed, some as quantitative measures of the presence of Gerrymandering, others as legal challenges to redistricting, and yet others as draft bills in Congress to minimize the effect of future redistricting. Many of the redistricting changes following the 2010 census used provisions of the Voting Rights Act (VRA) in way not intended by the people responsible for the VRA. For example, the VRA provided for majority-minority districts, which were intended as a means to ensure representation of minority groups in Congress. However, the VRA was used to "pack" districts, which promoted Gerrymandered districts, and also fewer majority-minority district than might otherwise have been possible.

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
        1. Use Cases (<strong>title</strong> and <strong>description</strong>) - <strong>PDF</strong><br>
        2. GUI State Page (electronically generated or hand-drawn) - <strong>PNG / JPEG</strong><br>
        3. README (<strong>optional</strong>) - <strong>PDF</strong>
      </td>
      <td>
        Dropbox sub-folder named <strong>Sep29</strong>.<br>
      </td>
    </tr>
    <tr>
      <td>10/13</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>10/20</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>10/17-11/5</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>11/17</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>11/24</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>11/18-11/26</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>12/1</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>12/5-12/17</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>?</td>
      <td></td>
      <td></td>
    </tr>
  </tbody>
</table>
