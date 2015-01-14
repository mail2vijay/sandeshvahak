package sandesh.encoding.utility;
public abstract class SafeThread extends Thread {

   String _id = null;
   long _startTime;


   public SafeThread() {}

   public SafeThread(String var1) {
      this._id = var1;
   }

   public abstract void safeRun();

   public void run() {
      try {
         this._startTime = System.currentTimeMillis();
         this.safeRun();
      } catch (Throwable var3) {
         String var2 = this.toString();
         if(this._id != null) {
            var2 = var2 + " [" + this._id + "]";
         }
   
      }

   }
}
