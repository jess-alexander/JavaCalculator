This Java Application is coded using NetBeans IDE by Jess Alexander with guidance from a 10 min YouTube video on implementing the Swing framework. 

My aim is to mimick the funtionality of the apple calculator app on my iPhone and MacBook Pro. I have re-structured the logic to accomplish the goal of supporting order of operations and have successfully tested 1+2=3. 

Basic logic is as follows: 

Upon the click of an operator button, push numbers entered into an ArrayList called numAL. 
Also push a 'key' into the calcAL ArrayList
Work through the array recursively, looping through multiplication and division first, then addition and subtraction
Use temporary ArrayLists to accomplish this goal so computing may continue if desired.
 
        //LOGIC GOAL
        // 0  1  2  3  4     numAL
        //     \ /
        //   +  *  -  /  =    calcAL
        // deleteCharAt(index1); deleteCharAt(index1+1); (calcAL should always?? end in 0 when entering this logic)
        // 0  2  3  4     numAL
        //        \ /
        //   +  -  /  =    calcAL
        // deleteCharAt(index2); deleteCharAt(index2+1); (calcAL should always?? end in 0 when entering this logic)
        // 0  2  .75     numAL
        //  \ /
        //   +  -   =    calcAL
        // deleteCharAt(index1); deleteCharAt(index1+1); (calcAL should always?? end in 0 when entering this logic)
        // 2  .75     numAL
        //  \ /
        //   -   =    calcAL
        // deleteCharAt(index2); deleteCharAt(index2+1); (calcAL should always?? end in 0 when entering this logic)
        //  1.25 

Finer details of logic will be explained as they are further tested.